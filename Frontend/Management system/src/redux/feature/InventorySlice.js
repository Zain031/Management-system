import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axios";

// Fetch products with pagination
export const fetchInventories = createAsyncThunk(
    "inventories/fetchInventories",
    async ({ page = 0, size = 10 } = {}, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.get(`/inventory?page=${page}&size=${size}`);
            console.log(response.data); // Debugging response
            return response; // Assume API returns { data: [...], paging: {...} }
        } catch (e) {
            console.error(e);
            const errorMessage = e.response?.data?.message || e.message || "Failed to fetch inventories";
            return rejectWithValue(errorMessage);
        }
    }
);

// Fetch product by ID
export const fetchInventoryById = createAsyncThunk(
    "inventories/fetchById",
    async (id, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.get(`/inventories/${id}`);
            return response.data;
        } catch (e) {
            return rejectWithValue(e.response?.data?.message || "Failed to fetch inventory by ID");
        }
    }
);

// Create a new product
export const createInventory = createAsyncThunk(
    "inventories/createInventory",
    async (inventory, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.post("/inventories/create", inventory);
            return response.data;
        } catch (e) {
            return rejectWithValue(e.response?.data?.message || "Failed to create inventory");
        }
    }
);

// Update an existing product
export const updateInventory = createAsyncThunk(
    "inventories/updateInventory",
    async (inventory, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.put(`/inventories/${inventory.id}`, inventory);
            return response.data;
        } catch (e) {
            return rejectWithValue(e.response?.data?.message || "Failed to update inventory");
        }
    }
);

// Delete a product
export const deleteInventory = createAsyncThunk(
    "inventories/deleteInventory",
    async (id, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.delete(`/inventories/delete/${id}`);

        } catch (e) {
            console.log(e);
            return rejectWithValue(e|| "Failed to delete inventory");
        }
    }
);

const InventoriesSlice = createSlice({
    name: "inventories",
    initialState: {
        inventories: [],
        paging: null, // For pagination info
        inventoryById: null,
        status: null,
        error: null,
    },
    extraReducers: (builder) => {
        builder
            // Fetch products
            .addCase(fetchInventories.fulfilled, (state, action) => {
                state.inventories = action.payload.data || [];
                state.paging = action.payload.paging || null;
                state.status = "succeeded";
            })
            .addCase(fetchInventories.rejected, (state, action) => {
                state.status = "failed";
                state.error = action.payload;
            })

            // Fetch product by ID
            .addCase(fetchInventoryById.fulfilled, (state, action) => {
                state.inventoryById = action.payload || null;
                state.status = "succeeded";
            })

            // Create product
            .addCase(createInventory.fulfilled, (state, action) => {
                state.inventories.push(action.payload);
                state.status = "succeeded";
            })

            // Update product
            .addCase(updateInventory.fulfilled, (state, action) => {
                const index = state.inventories.findIndex((p) => p.id === action.payload.id);
                if (index !== -1) {
                    state.inventories[index] = action.payload;
                }
                state.status = "succeeded";
            })

            // Delete product
            .addCase(deleteInventory.fulfilled, (state, action) => {
                state.inventories = state.inventories.filter((p) => p.id !== action.payload.id);
                state.status = "succeeded";
            })

            // Handle rejected cases globally
            .addMatcher(
                (action) => action.type.endsWith("/rejected"),
                (state, action) => {
                    state.error = action.payload;
                    state.status = "failed";
                }
            );
    },
});

export default InventoriesSlice.reducer;

