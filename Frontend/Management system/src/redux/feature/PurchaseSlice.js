import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axios";

export const fetchPurchases = createAsyncThunk(
    "purchases/fetchPurchases",
    async (params, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.get("/purchases", { params });
            return response.data;
        } catch (e) {
            return rejectWithValue(e || "Failed to fetch purchases");
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
            console.log(e);
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
            .addCase(fetchPurchases.fulfilled, (state, action) => {
                state.purchases = action.payload.data || [];
                state.paging = action.payload.paging;
                state.status = "succeeded";
            })
            .addCase(fetchPurchases.rejected, (state, action) => {
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
