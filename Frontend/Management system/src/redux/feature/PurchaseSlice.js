import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axios";
const getMonthName = (month) => {
  const months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];
  return months[month - 1];
};

export const fetchPurchasesPerMonthInRange = createAsyncThunk(
  "purchases/fetchPurchases",
  async ({ startDate, endDate } = {}, { rejectWithValue }) => {
    try {
      const now = new Date();
      const start = startDate
        ? new Date(startDate)
        : new Date(now.setFullYear(now.getFullYear(), now.getMonth() - 11, 1));
      const end = endDate ? new Date(endDate) : new Date();

      const dataPurchase = [];
      const startYear = start.getFullYear();
      const startMonth = start.getMonth();
      const endYear = end.getFullYear();
      const endMonth = end.getMonth();

      for (
        let currentYear = startYear, currentMonth = startMonth;
        currentYear < endYear ||
        (currentYear === endYear && currentMonth <= endMonth);
        currentMonth++
      ) {
        if (currentMonth > 11) {
          currentMonth = 0;
          currentYear++;
        }
        const monthName = getMonthName(currentMonth + 1);
        const response = await axiosInstance.get(
          `/purchases/month/${monthName}?years=${currentYear}`
        );
        dataPurchase.push({
          year: currentYear,
          month: monthName,
          purchases: response.data.data,
        });
      }

      dataPurchase.sort(
        (a, b) =>
          new Date(a.year, getMonthName(a.month) - 1) -
          new Date(b.year, getMonthName(b.month) - 1)
      );

      return dataPurchase.map((item) => item.purchases);
    } catch (error) {
      return rejectWithValue(error?.message || "Failed to fetch purchases");
    }
  }
);

export const fetchPurchasesPerDay = createAsyncThunk(
  "purchases/fetchPurchasesPerDay",
  async ({ startDate, endDate } = {}, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/purchases?startDate=${startDate}&endDate=${endDate}`
      );
      return response.data;
    } catch (e) {
      return rejectWithValue(e || "Failed to fetch purchases per day");
    }
  }
);

export const fetchPurchaseById = createAsyncThunk(
  "purchases/fetchById",
  async (id, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(`/purchases/${id}`);
      return response.data;
    } catch (e) {
      return rejectWithValue(e || "Failed to fetch purchase by ID");
    }
  }
);

export const createPurchase = createAsyncThunk(
  "purchases/createPurchase",
  async (purchase, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.post(`/purchases`, purchase);
      return response.data;
    } catch (e) {
      return rejectWithValue(e || "Failed to create purchase");
    }
  }
);

export const updatePurchase = createAsyncThunk(
  "purchases/updatePurchase",
  async (purchase, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.put(`/purchases`, purchase);
      return response.data;
    } catch (e) {
      return rejectWithValue(e || "Failed to update purchase");
    }
  }
);

export const deletePurchase = createAsyncThunk(
  "purchases/deletePurchase",
  async (id, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.delete(`/purchases/${id}`);
      return response.data;
    } catch (e) {
      return rejectWithValue(e || "Failed to delete purchase");
    }
  }
);

const PurchaseSlice = createSlice({
  name: "purchases",
  initialState: {
    purchases: [],
    purchaseById: null,
    status: null,
  },

  extraReducers: (builder) => {
    builder
      .addCase(fetchPurchasesPerMonthInRange.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchPurchasesPerMonthInRange.fulfilled, (state, action) => {
        state.purchases = action.payload || [];
        state.status = "succeeded";
      })
      .addCase(fetchPurchasesPerMonthInRange.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(fetchPurchaseById.fulfilled, (state, action) => {
        const packageWeddingById = action.payload.data;
        state.purchaseById = packageWeddingById;
        state.status = "succeeded";
      })
      .addCase(createPurchase.fulfilled, (state, action) => {
        state.purchases.push(action.payload);
        state.status = "succeeded";
      })
      .addCase(updatePurchase.fulfilled, (state, action) => {
        const index = state.purchases.findIndex(
          (pkg) => pkg.id === action.payload.id
        );
        if (index !== -1) {
          state.purchases[index] = action.payload;
        }
        state.status = "succeeded";
      })

      .addCase(deletePurchase.fulfilled, (state, action) => {
        state.purchases = state.purchases.filter(
          (pkg) => pkg.id !== action.payload.id
        );
        state.status = "succeeded";
      })
      .addMatcher(
        (action) => action.type.endsWith("/rejected"),
        (state, action) => {
          state.error = action.payload;
          state.status = "failed";
        }
      );
  },
});

export default PurchaseSlice.reducer;
