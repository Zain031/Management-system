import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axios";

export const exportUsers = createAsyncThunk(
  "export/exportUsers",
  async (_, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get("/auth/export-pdf", {
        responseType: "blob",
      });

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "users.pdf"); // Nama file
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);

      return true;
    } catch (e) {
      return rejectWithValue(e.message || "Failed to export users");
    }
  }
);

export const exportProducts = createAsyncThunk(
  "export/exportProducts",
  async (_, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get("/products/export-pdf", {
        responseType: "blob",
      });

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "products.pdf");
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);

      return true;
    } catch (e) {
      return rejectWithValue(e.message || "Failed to export products");
    }
  }
);

export const exportInventoryPerMonth = createAsyncThunk(
  "export/exportInventoryPerMonth",
  async ({ year, month }, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/inventory/export-pdf-month/${month}`,
        {
          params: { year },
          responseType: "blob",
        }
      );

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `inventory-${year}-${month}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);
      window.URL.revokeObjectURL(url); // Clean up URL

      return true;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || e.message || "Failed to export inventory"
      );
    }
  }
);

export const exportInventoryPerDate = createAsyncThunk(
  "export/exportInventoryPerDate",
  async ({ date }, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/inventory/export-pdf-date/${date}`,
        {
          responseType: "blob",
        }
      );

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `inventory-${date}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);
      window.URL.revokeObjectURL(url); // Clean up URL

      return true;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || e.message || "Failed to export inventory"
      );
    }
  }
);

export const exportPaymentReceipt = createAsyncThunk(
  "export/exportPaymentReceipt",
  async (id, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/payment/generate-receipt/${id}`,
        {
          responseType: "blob",
        }
      );

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `payment-receipt-${id}-${Date.now()}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);

      return true;
    } catch (e) {
      return rejectWithValue(e.message || "Failed to export payment receipt");
    }
  }
);

export const exportOrdersPerMonth = createAsyncThunk(
  "export/exportOrderPerMonth",
  async ({ year, month }, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/orders/export-pdf-month/${month}?year=${year}`,
        {
          responseType: "blob",
        }
      );

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "orders.pdf");
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);

      return true;
    } catch (e) {
      return rejectWithValue(e.message || "Failed to export orders");
    }
  }
);

export const exportOrdersPerDate = createAsyncThunk(
  "export/exportOrderPerDate",
  async ({ date }, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/orders/export-pdf-date/${date}`,
        {
          responseType: "blob",
        }
      );

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "orders.pdf");
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);

      return true;
    } catch (e) {
      return rejectWithValue(e.message || "Failed to export orders");
    }
  }
);

const exportSlice = createSlice({
  name: "export",
  initialState: {
    loading: false,
    error: null,
    success: null,
  },
  reducers: {
    clearExportStatus: (state) => {
      state.error = null;
      state.success = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(exportUsers.pending, (state) => {
        state.loading = true;
        state.error = null;
        state.success = null;
      })
      .addCase(exportUsers.fulfilled, (state) => {
        state.loading = false;
        state.success = "Users exported successfully!";
      })
      .addCase(exportUsers.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(exportProducts.pending, (state) => {
        state.loading = true;
        state.error = null;
        state.success = null;
      })
      .addCase(exportProducts.fulfilled, (state) => {
        state.loading = false;
        state.success = "Products exported successfully!";
      })
      .addCase(exportProducts.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(exportInventoryPerMonth.pending, (state) => {
        state.loading = true;
        state.error = null;
        state.success = null;
      })
      .addCase(exportInventoryPerMonth.fulfilled, (state) => {
        state.loading = false;
        state.success = "Inventory exported successfully!";
      })
      .addCase(exportInventoryPerMonth.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(exportInventoryPerDate.pending, (state) => {
        state.loading = true;
        state.error = null;
        state.success = null;
      })
      .addCase(exportInventoryPerDate.fulfilled, (state) => {
        state.loading = false;
        state.success = "Inventory exported successfully!";
      })
      .addCase(exportInventoryPerDate.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(exportPaymentReceipt.pending, (state) => {
        state.loading = true;
        state.error = null;
        state.success = null;
      })
      .addCase(exportPaymentReceipt.fulfilled, (state) => {
        state.loading = false;
        state.success = "Payment receipt exported successfully!";
      })
      .addCase(exportPaymentReceipt.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(exportOrdersPerMonth.pending, (state) => {
        state.loading = true;
        state.error = null;
        state.success = null;
      })
      .addCase(exportOrdersPerMonth.fulfilled, (state) => {
        state.loading = false;
        state.success = "Orders exported successfully!";
      })
      .addCase(exportOrdersPerMonth.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(exportOrdersPerDate.pending, (state) => {
        state.loading = true;
        state.error = null;
        state.success = null;
      })
      .addCase(exportOrdersPerDate.fulfilled, (state) => {
        state.loading = false;
        state.success = "Orders exported successfully!";
      })
      .addCase(exportOrdersPerDate.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
  },
});

export const { clearExportStatus } = exportSlice.actions;
export default exportSlice.reducer;
