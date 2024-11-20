import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import axiosInstance from '../../api/axios'

export const fetchSales = createAsyncThunk(
  'sales/fetchSales',
  async (params, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get('/sales', { params })
      return response.data
    } catch (e) {
      return rejectWithValue(e || 'Failed to fetch sales')
    }
  },
)

export const fetchSaleById = createAsyncThunk(
  'sales/fetchById',
  async (id, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(`/sales/${id}`)
      return response.data
    } catch (e) {
      return rejectWithValue(e || 'Failed to fetch sale by ID')
    }
  },
)

export const createSale = createAsyncThunk(
  'sales/createSale',
  async (sale, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.post(`/sales`, sale)
      return response.data
    } catch (e) {
      return rejectWithValue(e || 'Failed to create sale')
    }
  },
)

export const updateSale = createAsyncThunk(
  'sales/updateSale',
  async (sale, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.put(`/sales`, sale)
      return response.data
    } catch (e) {
      return rejectWithValue(e || 'Failed to update sale')
    }
  },
)

export const deleteSale = createAsyncThunk(
  'sales/deleteSale',
  async (id, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.delete(`/sales/${id}`)
      return response.data
    } catch (e) {
      console.log(e)
      return rejectWithValue(e || 'Failed to delete sale')
    }
  },
)


const SalesSlice = createSlice({
  name: 'sales',
  initialState: {
    sales: [],
    saleById: null,
    status: null,
  },

  extraReducers: (builder) => {
    builder
      .addCase(fetchSales.fulfilled, (state, action) => {
        state.sales = action.payload.data || []
        state.paging = action.payload.paging
        state.status = 'succeeded'
      })
      .addCase(fetchSales.rejected, (state, action) => {
        state.status = 'failed'
        state.error = action.payload
      })
      .addCase(fetchSaleById.fulfilled, (state, action) => {
        const packageWeddingById = action.payload.data
        state.saleById = packageWeddingById
        state.status = 'succeeded'
      })
      .addCase(createSale.fulfilled, (state, action) => {
        state.sales.push(action.payload)
        state.status = 'succeeded'
      })
      .addCase(updateSale.fulfilled, (state, action) => {
        const index = state.sales.findIndex((pkg) => pkg.id === action.payload.id)
        if (index !== -1) {
          state.sales[index] = action.payload
        }
        state.status = 'succeeded'
      })
      .addCase(updateSaleImage.fulfilled, (state, action) => {
        const index = state.sales.findIndex((pkg) => pkg.id === action.payload.id)
        if (index !== -1) {
          state.sales[index] = action.payload
        }
        state.status = 'succeeded'
      })
      .addCase(deleteSaleImage.fulfilled, (state, action) => {
        const index = state.sales.findIndex((pkg) => pkg.id === action.payload.id)
        if (index !== -1) {
          state.sales[index] = action.payload
        }
        state.status = 'succeeded'
      })
      .addCase(deleteSale.fulfilled, (state, action) => {
        state.sales = state.sales.filter((pkg) => pkg.id !== action.payload.id)
        state.status = 'succeeded'
      })
      .addMatcher(
        (action) => action.type.endsWith('/rejected'),
        (state, action) => {
          state.error = action.payload
          state.status = 'failed'
        },
      )
  },
})

export default SalesSlice.reducer

