import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axios";

// Fetch products with pagination
export const fetchInventories = createAsyncThunk(
  "inventories/fetchInventories",
  async ({ page = 1, size = 10 } = {}, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/inventory?page=${page - 1}&size=${size}`
      );
      return response.data;
    } catch (e) {
      const errorMessage =
        e.response?.data?.message || e.message || "Failed to fetch inventories";
      return rejectWithValue(errorMessage);
    }
  }
);

// Fetch product by ID
export const fetchInventoryById = createAsyncThunk(
  "inventories/fetchById",
  async (id, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(`/inventory/${id}`);
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to fetch inventory by ID"
      );
    }
  }
);

export const fetchInventoryByName = createAsyncThunk(
  "inventories/fetchByName",
  async ({ name, page = 1, size = 10 }, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/inventory/search/${name}?page=${page - 1}&size=${size}`
      );
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to fetch inventories by name"
      );
    }
  }
);

export const fetchInventoryByCategory = createAsyncThunk(
  "inventories/fetchByCategory",
  async ({ category, page = 1, size = 10 }, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/inventory/category/${category}?page=${page - 1}&size=${size}`
      );
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to fetch inventories by category"
      );
    }
  }
);

// Create a new product
export const createInventory = createAsyncThunk(
  "inventories/createInventory",
  async (inventory, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.post("/inventory/create", inventory);
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to create inventory"
      );
    }
  }
);

// Update an existing product
export const updateInventory = createAsyncThunk(
  "inventories/updateInventory",
  async ({ id, data }, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.put(`/inventory/update/${id}`, data);
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to update inventory"
      );
    }
  }
);

// Delete a product
export const deleteInventory = createAsyncThunk(
  "inventories/deleteInventory",
  async (id, { rejectWithValue }) => {
    try {
      await axiosInstance.delete(`/inventory/delete/${id}`);
      return id;
    } catch (e) {
      console.log(e);
      return rejectWithValue(e || "Failed to delete inventory");
    }
  }
);

const InventoriesSlice = createSlice({
  name: "inventories",
  initialState: {
    inventories: [],
    paging: null,
    page: 1,
    inventoryById: null,
    status: null,
    error: null,
  },
  reducers: {
    setPage: (state, action) => {
      state.page = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch products
      .addCase(fetchInventories.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchInventories.fulfilled, (state, action) => {
        state.inventories = action.payload.data.content || [];
        state.paging = {
          totalElements: action.payload.data.totalElements,
          totalPages: action.payload.data.totalPages,
        };
        state.status = "succeeded";
      })
      .addCase(fetchInventories.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })

      // Fetch product by ID
      .addCase(fetchInventoryById.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchInventoryById.fulfilled, (state, action) => {
        state.inventoryById = action.payload.data || null;
        state.status = "succeeded";
      })
      .addCase(fetchInventoryById.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })

      // Fetch product by name
      .addCase(fetchInventoryByName.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchInventoryByName.fulfilled, (state, action) => {
        state.inventories = action.payload.data.content || [];
        state.paging = {
          totalElements: action.payload.data.totalElements,
          totalPages: action.payload.data.totalPages,
        };
        state.status = "succeeded";
      })
      .addCase(fetchInventoryByName.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })

      // fetch product by category
      .addCase(fetchInventoryByCategory.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchInventoryByCategory.fulfilled, (state, action) => {
        state.inventories = action.payload.data.content || [];
        state.paging = {
          totalElements: action.payload.data.totalElements,
          totalPages: action.payload.data.totalPages,
        };
        state.status = "succeeded";
      })
      .addCase(fetchInventoryByCategory.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })

      // Create product
      .addCase(createInventory.pending, (state) => {
        state.status = "loading";
      })
      .addCase(createInventory.fulfilled, (state, action) => {
        state.inventories.push(action.payload.data);
        state.status = "succeeded";
      })
      .addCase(createInventory.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })

      // Update product
      .addCase(updateInventory.pending, (state) => {
        state.status = "loading";
      })
      .addCase(updateInventory.fulfilled, (state, action) => {
        const index = state.inventories.findIndex(
          (p) => p.id_material === action.payload.data.id_material
        );
        if (index !== -1) {
          state.inventories[index] = action.payload.data;
        }
        state.status = "succeeded";
      })
      .addCase(updateInventory.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })

      // Delete product
      .addCase(deleteInventory.pending, (state) => {
        state.status = "loading";
      })
      .addCase(deleteInventory.fulfilled, (state, action) => {
        state.inventories = state.inventories.filter(
          (p) => p.id !== action.payload.id
        );
        state.status = "succeeded";
      })
      .addCase(deleteInventory.rejected, (state, action) => {
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

export const { setPage } = InventoriesSlice.actions;
export default InventoriesSlice.reducer;
