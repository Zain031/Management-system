import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axios";
import axios from "axios";

export const fetchFoods = createAsyncThunk(
    "foods/fetchFoods",
    async (_, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.get("/foods/");
            return response.data;
        } catch (e) {
            return rejectWithValue(e || "Failed to fetch foods");
        }
    }
);

export const fetchFoodById = createAsyncThunk(
    "foods/fetchById",
    async (id, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.get(`/foods/${id}`);
            return response.data;
        } catch (e) {
            return rejectWithValue(e || "Failed to fetch food by ID");
        }
    }
);

export const createFood = createAsyncThunk(
    "foods/createFood",
    async (food, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.post(`/foods`, food);
            return response.data;
        } catch (e) {
            return rejectWithValue(e || "Failed to create food");
        }
    }
);

export const updateFood = createAsyncThunk(
    "foods/updateFood",
    async (food, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.put(`/foods`, food);
            return response.data;
        } catch (e) {
            return rejectWithValue(e || "Failed to update food");
        }
    }
);

export const deleteFood = createAsyncThunk(
    "foods/deleteFood",
    async (id, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.delete(`/foods/delete/${id}`);
            console.log("🚀 ~ response:", response);

            return response.data;
        } catch (e) {
            console.log(e);
            return rejectWithValue(e || "Failed to delete food");
        }
    }
);

const FoodSlice = createSlice({
    name: "food",
    initialState: {
        foods: [],
        foodById: null,
        status: null,
    },

    extraReducers: (builder) => {
        builder
            .addCase(fetchFoods.fulfilled, (state, action) => {
                state.foods = action.payload.data || [];
                state.paging = action.payload.paging;
                state.status = "succeeded";
            })
            .addCase(fetchFoods.rejected, (state, action) => {
                state.status = "failed";
                state.error = action.payload;
            })
            .addCase(fetchFoodById.fulfilled, (state, action) => {
                const packageWeddingById = action.payload.data;
                state.foodById = packageWeddingById;
                state.status = "succeeded";
            })
            .addCase(createFood.fulfilled, (state, action) => {
                state.foods.push(action.payload);
                state.status = "succeeded";
            })
            .addCase(updateFood.fulfilled, (state, action) => {
                const index = state.foods.findIndex(
                    (pkg) => pkg.id === action.payload.id
                );
                if (index !== -1) {
                    state.foods[index] = action.payload;
                }
                state.status = "succeeded";
            })

            .addCase(deleteFood.fulfilled, (state, action) => {
                state.foods = state.foods.filter(
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

export default FoodSlice.reducer;
