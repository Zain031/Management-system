import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axios";

// Fetch products with pagination
export const fetchProducts = createAsyncThunk(
    "products/fetchProducts",
    async ({ page = 0, size = 10 } = {}, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.get(`/products?page=${page}&size=${size}`);
            console.log(response.data); // Debugging response
            return response; // Assume API returns { data: [...], paging: {...} }
        } catch (e) {
            console.error(e);
            const errorMessage = e.response?.data?.message || e.message || "Failed to fetch products";
            return rejectWithValue(errorMessage);
        }
    }
);

// Fetch product by ID
export const fetchProductById = createAsyncThunk(
    "products/fetchById",
    async (id, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.get(`/products/${id}`);
            return response.data;
        } catch (e) {
            return rejectWithValue(e.response?.data?.message || "Failed to fetch product by ID");
        }
    }
);

// Create a new product
export const createProduct = createAsyncThunk(
    "products/createProduct",
    async (product, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.post("/products/create", product);
            return response.data;
        } catch (e) {
            return rejectWithValue(e.response?.data?.message || "Failed to create product");
        }
    }
);

// Update an existing product
export const updateProduct = createAsyncThunk(
    "products/updateProduct",
    async (product, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.put(`/products/${product.id}`, product);
            return response.data;
        } catch (e) {
            return rejectWithValue(e.response?.data?.message || "Failed to update product");
        }
    }
);

// Delete a product
export const deleteProduct = createAsyncThunk(
    "products/deleteProduct",
    async (id, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.delete(`/products/delete/${id}`);

        } catch (e) {
            console.log(e);
            return rejectWithValue(e|| "Failed to delete product");
        }
    }
);

const ProductsSlice = createSlice({
    name: "products",
    initialState: {
        products: [],
        paging: null, // For pagination info
        productById: null,
        status: null,
        error: null,
    },
    extraReducers: (builder) => {
        builder
            // Fetch products
            .addCase(fetchProducts.fulfilled, (state, action) => {
                state.products = action.payload.data || [];
                state.paging = action.payload.paging || null;
                state.status = "succeeded";
            })
            .addCase(fetchProducts.rejected, (state, action) => {
                state.status = "failed";
                state.error = action.payload;
            })

            // Fetch product by ID
            .addCase(fetchProductById.fulfilled, (state, action) => {
                state.productById = action.payload || null;
                state.status = "succeeded";
            })

            // Create product
            .addCase(createProduct.fulfilled, (state, action) => {
                state.products.push(action.payload);
                state.status = "succeeded";
            })

            // Update product
            .addCase(updateProduct.fulfilled, (state, action) => {
                const index = state.products.findIndex((p) => p.id === action.payload.id);
                if (index !== -1) {
                    state.products[index] = action.payload;
                }
                state.status = "succeeded";
            })

            // Delete product
            .addCase(deleteProduct.fulfilled, (state, action) => {
                state.products = state.products.filter((p) => p.id !== action.payload.id);
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

export default ProductsSlice.reducer;
