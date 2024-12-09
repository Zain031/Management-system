import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axios";

export const fetchDashboardData = createAsyncThunk(
  "dashboard/fetchDashboardData",
  async (year, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/business-performance/years/${year}`
      );
      return response.data;
    } catch (e) {
      return rejectWithValue(e || "Failed to fetch dashboard data");
    }
  }
);

const dashboardSlice = createSlice({
  name: "dashboard",
  initialState: {
    monthlyPerformance: [],
    totalProfit: 0,
    totalPriceOrder: 0,
    totalOrder: 0,
    totalPriceInventory: 0,
    year: 2024,

    loading: false,
    error: null,
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchDashboardData.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchDashboardData.fulfilled, (state, action) => {
        state.monthlyPerformance = action.payload.data.monthly_performance.map(
          (item) => {
            return {
              month: item.month.slice(0, 3) + " " + action.payload.data.years,
              profit: item.profit,
              totalOrder: item.total_order,
              totalPriceOrder: item.total_price_order,
              totalPriceInventory: item.total_price_inventory,
            };
          }
        );
        state.totalProfit = action.payload.data.total_profit;
        state.totalPriceOrder = action.payload.data.grand_total_price_order;
        state.totalOrder = action.payload.data.grand_total_order;
        state.totalPriceInventory =
          action.payload.data.grand_total_price_inventory;
        state.year = action.payload.data.years;
        state.loading = false;
      })
      .addCase(fetchDashboardData.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload.data;
      });
  },
});

export default dashboardSlice.reducer;
