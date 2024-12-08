import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axios";

export const exportUsers = createAsyncThunk(
  "export/exportUsers",
  async (_, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get("/auth/export-pdf", {
        responseType: "blob", // Pastikan respons berupa file (PDF/Excel/CSV)
      });

      // Buat Blob URL
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "users.pdf"); // Nama file
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);

      return true; // Kembalikan nilai jika perlu
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

      // Buat Blob URL
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

const exportSlice = createSlice({
  name: "export",
  initialState: {
    loading: false,
    error: null,
    success: null, // Tambahkan untuk status berhasil
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
      });
  },
});

export const { clearExportStatus } = exportSlice.actions;
export default exportSlice.reducer;
