import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axios";

export const fetchOrders = createAsyncThunk(
  "orders/fetchOrders",
  async ({ page = 1, size = 10 } = {}, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/orders?page=${page - 1}&size=${size}`
      );
      return response.data;
    } catch (e) {
      const errorMessage =
        e.response?.data?.message || e.message || "Failed to fetch orders";
      return rejectWithValue(errorMessage);
    }
  }
);

export const fetchOrderById = createAsyncThunk(
  "orders/fetchById",
  async (id, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(`/orders/${id}`);
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to fetch order by ID"
      );
    }
  }
);

export const createOrder = createAsyncThunk(
  "orders/createOrder",
  async (order, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.post(`/orders/create`, order);
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to create order"
      );
    }
  }
);

export const fetchOrderByMonth = createAsyncThunk(
  "orders/fetchByMonth",
  async ({ month, year }, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/orders/month/${month}?year=${year}`
      );
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to fetch order by month"
      );
    }
  }
);
const orderSlice = createSlice({
  name: "orders",
  initialState: {
    orders: [],
    paging: {},
    order: {},
    loading: false,
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchOrders.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchOrders.fulfilled, (state, action) => {
        state.loading = false;
        state.orders = action.payload.data.content;
      })
      .addCase(fetchOrders.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(fetchOrderById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchOrderById.fulfilled, (state, action) => {
        state.loading = false;
        state.order = action.payload.data;
      })
      .addCase(fetchOrderById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(fetchOrderByMonth.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchOrderByMonth.fulfilled, (state, action) => {
        state.loading = false;
        state.orders = action.payload.data;
      })
      .addCase(fetchOrderByMonth.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(createOrder.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createOrder.fulfilled, (state, action) => {
        state.loading = false;
        state.orders.push(action.payload.data);
      })
      .addCase(createOrder.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
  },
});

export default orderSlice.reducer;
