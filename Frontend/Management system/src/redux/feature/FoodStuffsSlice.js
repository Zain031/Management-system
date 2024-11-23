import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axios";

export const fetchFoodStuffs = createAsyncThunk(
    "foodStuffs/fetchFoodStuffs",
    async (params, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.get("/food-stuffs", {
                params,
            });
            return response.data;
        } catch (e) {
            return rejectWithValue(e || "Failed to fetch food-stuffs");
        }
    }
);

export const fetchFoodStuffById = createAsyncThunk(
    "foodStuffs/fetchById",
    async (id, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.get(`/food-stuffs/${id}`);
            return response.data;
        } catch (e) {
            return rejectWithValue(e || "Failed to fetch food-stuff by ID");
        }
    }
);

export const createFoodStuff = createAsyncThunk(
    "foodStuffs/createFoodStuff",
    async (foodStuff, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.post(
                `/food-stuffs`,
                foodStuff
            );
            return response.data;
        } catch (e) {
            return rejectWithValue(e || "Failed to create food-stuff");
        }
    }
);

export const updateFoodStuff = createAsyncThunk(
    "foodStuffs/updateFoodStuff",
    async (foodStuff, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.put(`/food-stuffs`, foodStuff);
            return response.data;
        } catch (e) {
            return rejectWithValue(e || "Failed to update food-stuff");
        }
    }
);

export const deleteFoodStuff = createAsyncThunk(
    "foodStuffs/deleteFoodStuff",
    async (id, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.delete(`/food-stuffs/${id}`);
            return response.data;
        } catch (e) {
            console.log(e);
            return rejectWithValue(e || "Failed to delete food-stuff");
        }
    }
);

const FoodStuffSlice = createSlice({
    name: "foodStuff",
    initialState: {
        foodStuffs: [],
        foodStuffById: null,
        status: null,
    },

    extraReducers: (builder) => {
        builder
            .addCase(fetchFoodStuffs.fulfilled, (state, action) => {
                state.foodStuffs = action.payload.data || [];
                state.paging = action.payload.paging;
                state.status = "succeeded";
            })
            .addCase(fetchFoodStuffs.rejected, (state, action) => {
                state.status = "failed";
                state.error = action.payload;
            })
            .addCase(fetchFoodStuffById.fulfilled, (state, action) => {
                const packageWeddingById = action.payload.data;
                state.foodStuffById = packageWeddingById;
                state.status = "succeeded";
            })
            .addCase(createFoodStuff.fulfilled, (state, action) => {
                state.foodStuffs.push(action.payload);
                state.status = "succeeded";
            })
            .addCase(updateFoodStuff.fulfilled, (state, action) => {
                const index = state.foodStuffs.findIndex(
                    (pkg) => pkg.id === action.payload.id
                );
                if (index !== -1) {
                    state.foodStuffs[index] = action.payload;
                }
                state.status = "succeeded";
            })
            .addCase(deleteFoodStuff.fulfilled, (state, action) => {
                state.foodStuffs = state.foodStuffs.filter(
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

export default FoodStuffSlice.reducer;
