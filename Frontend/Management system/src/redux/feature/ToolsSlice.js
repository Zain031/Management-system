import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import axiosInstance from '../../api/axios'

export const fetchTools = createAsyncThunk(
  'tools/fetchTools',
  async (params, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get('/tools', { params })
      return response.data
    } catch (e) {
      return rejectWithValue(e || 'Failed to fetch tools')
    }
  },
)

export const fetchToolById = createAsyncThunk(
  'tools/fetchById',
  async (id, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(`/tools/${id}`)
      return response.data
    } catch (e) {
      return rejectWithValue(e || 'Failed to fetch tool by ID')
    }
  },
)

export const createTool = createAsyncThunk(
  'tools/createTool',
  async (tool, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.post(`/tools`, tool)
      return response.data
    } catch (e) {
      return rejectWithValue(e || 'Failed to create tool')
    }
  },
)

export const updateTool = createAsyncThunk(
  'tools/updateTool',
  async (tool, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.put(`/tools`, tool)
      return response.data
    } catch (e) {
      return rejectWithValue(e || 'Failed to update tool')
    }
  },
)

export const deleteTool = createAsyncThunk(
  'tools/deleteTool',
  async (id, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.delete(`/tools/${id}`)
      return response.data
    } catch (e) {
      console.log(e)
      return rejectWithValue(e || 'Failed to delete tool')
    }
  },
)


const ToolsSlice = createSlice({
  name: 'tools',
  initialState: {
    tools: [],
    toolById: null,
    status: null,
  },

  extraReducers: (builder) => {
    builder
      .addCase(fetchTools.fulfilled, (state, action) => {
        state.tools = action.payload.data || []
        state.paging = action.payload.paging
        state.status = 'succeeded'
      })
      .addCase(fetchTools.rejected, (state, action) => {
        state.status = 'failed'
        state.error = action.payload
      })
      .addCase(fetchToolById.fulfilled, (state, action) => {
        const toolById = action.payload.data
        state.toolById = toolById
        state.status = 'succeeded'
      })
      .addCase(createTool.fulfilled, (state, action) => {
        state.tools.push(action.payload)
        state.status = 'succeeded'
      })
      .addCase(updateTool.fulfilled, (state, action) => {
        const index = state.tools.findIndex((tool) => tool.id === action.payload.id)
        if (index !== -1) {
          state.tools[index] = action.payload
        }
        state.status = 'succeeded'
      })
      .addCase(deleteTool.fulfilled, (state, action) => {
        state.tools = state.tools.filter((tool) => tool.id !== action.payload.id)
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

export default ToolsSlice.reducer

