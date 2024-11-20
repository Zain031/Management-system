import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import axiosInstance from '../../api/axios'

export const fetchProfits = createAsyncThunk(
  'profits/fetchProfits',
  async (params, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get('/profits', { params })
      return response.data
    } catch (e) {
      return rejectWithValue(e || 'Failed to fetch profits')
    }
  },
)

export const fetchProfitByDate = createAsyncThunk(
  'profits/fetchByDate',
  async (date, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(`/profits/${date}`)
      return response.data
    } catch (e) {
      return rejectWithValue(e || 'Failed to fetch profit by date')
    }
  },
)



const ProfitsSlice = createSlice({
  name: 'profits',
  initialState: {
    profits: [],
    profitById: null,
    status: null,
  },

  extraReducers: (builder) => {
    builder
      .addCase(fetchProfits.fulfilled, (state, action) => {
        state.profits = action.payload.data || []
        state.paging = action.payload.paging
        state.status = 'succeeded'
      })
      .addCase(fetchProfits.rejected, (state, action) => {
        state.status = 'failed'
        state.error = action.payload
      })
      .addCase(fetchProfitByDate.fulfilled, (state, action) => {
        const packageWeddingById = action.payload.data
        state.profitById = packageWeddingById
        state.status = 'succeeded'
      })
      .addCase(fetchProfitByDate.rejected, (state, action) => {
        state.status = 'failed'
        state.error = action.payload
      })
      .addMatcher(
        (action) => action.type.endsWith('/pending'),
        (state) => {
          state.status = 'loading'
        },
      )
      .addMatcher(
        (action) => action.type.endsWith('/fulfilled'),
        (state) => {
          state.status = 'succeeded'
        },
      )
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

