import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axios";

// Fetch products with pagination
export const fetchProducts = createAsyncThunk(
  "products/fetchProducts",
  async ({ page = 1, size = 10 } = {}, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/products?page=${page - 1}&size=${size}`
      );
      return response.data;
    } catch (e) {
      const errorMessage =
        e.response?.data?.message || e.message || "Failed to fetch products";
      return rejectWithValue(errorMessage);
    }
  }
);

export const fetchProductsByCategory = createAsyncThunk(
  "products/fetchProductsByCategory",
  async (category, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/products/category/${category}`
      );
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to fetch products"
      );
    }
  }
);

export const fetchProductsByName = createAsyncThunk(
  "products/fetchProductsBySearch",
  async (searchTerm, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/products/search/${searchTerm}`
      );
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to fetch products"
      );
    }
  }
);

export const fetchProductsAvailable = createAsyncThunk(
  "products/fetchProductsAvailable",
  async ({ page = 1, size = 10 } = {}, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/products/availability?page=${page - 1}&size=${size}`
      );
      return response.data;
    } catch (e) {
      console.error(e);
      const errorMessage =
        e.response?.data?.message || e.message || "Failed to fetch products";
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
      return rejectWithValue(
        e.response?.data?.message || "Failed to fetch product by ID"
      );
    }
  }
);

// Create a new product
export const createProduct = createAsyncThunk(
  "products/createProduct",
  async (product, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.post("/products/create", product);
      console.log("Creating product", response.data);
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to create product"
      );
    }
  }
);

// Update an existing product
export const updateProduct = createAsyncThunk(
  "products/updateProduct",
  async (product, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.put(
        `/products/${product.id}`,
        product
      );
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to update product"
      );
    }
  }
);

// Delete a product
export const deleteProduct = createAsyncThunk(
  "products/deleteProduct",
  async (id, { rejectWithValue }) => {
    try {
      await axiosInstance.delete(`/products/delete/${id}`);
      return id;
    } catch (e) {
      console.log(e);
      return rejectWithValue(e || "Failed to delete product");
    }
  }
);

const ProductsSlice = createSlice({
  name: "products",
  initialState: {
    products: [],
    paging: null,
    productById: null,
    status: null,
    error: null,
  },
  extraReducers: (builder) => {
    builder
      // Fetch products
      .addCase(fetchProducts.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchProducts.fulfilled, (state, action) => {
        state.products = action.payload.data.content || [];
        state.paging = {
          totalElements: action.payload.data.totalElements,
          totalPages: action.payload.data.totalPages,
        };
        state.status = "succeeded";
      })
      .addCase(fetchProducts.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })

      // Fetch products by category
      .addCase(fetchProductsByCategory.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchProductsByCategory.fulfilled, (state, action) => {
        state.products = action.payload.data.content || [];
        state.paging = {
          totalElements: action.payload.data.totalElements,
          totalPages: action.payload.data.totalPages,
        };
        state.status = "succeeded";
      })
      .addCase(fetchProductsByCategory.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })

      // Fetch products by name
      .addCase(fetchProductsByName.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchProductsByName.fulfilled, (state, action) => {
        state.products = action.payload.data.content || [];
        state.paging = {
          totalElements: action.payload.data.totalElements,
          totalPages: action.payload.data.totalPages,
        };
        state.status = "succeeded";
      })
      .addCase(fetchProductsByName.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })

      // Fetch products available
      .addCase(fetchProductsAvailable.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchProductsAvailable.fulfilled, (state, action) => {
        state.products = action.payload.data.content || [];
        state.paging = {
          totalElements: action.payload.data.totalElements,
          totalPages: action.payload.data.totalPages,
        };
        state.status = "succeeded";
      })
      .addCase(fetchProductsAvailable.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })

      // Fetch product by ID
      .addCase(fetchProductById.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchProductById.fulfilled, (state, action) => {
        state.productById = action.payload || null;
        state.status = "succeeded";
      })
      .addCase(fetchProductById.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })

      // Create product
      .addCase(createProduct.pending, (state) => {
        state.status = "loading";
      })
      .addCase(createProduct.fulfilled, (state, action) => {
        state.products.push(action.payload.data);
        state.status = "succeeded";
      })
      .addCase(createProduct.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })

      // Update product
      .addCase(updateProduct.pending, (state) => {
        state.status = "loading";
      })
      .addCase(updateProduct.fulfilled, (state, action) => {
        const index = state.products.findIndex(
          (p) => p.id === action.payload.id
        );
        if (index !== -1) {
          state.products[index] = action.payload;
        }
        state.status = "succeeded";
      })
      .addCase(updateProduct.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })

      // Delete product
      .addCase(deleteProduct.pending, (state) => {
        state.status = "loading";
      })
      .addCase(deleteProduct.fulfilled, (state, action) => {
        state.products = state.products.filter(
          (p) => p.id !== action.payload.id
        );
        state.status = "succeeded";
      })
      .addCase(deleteProduct.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
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
