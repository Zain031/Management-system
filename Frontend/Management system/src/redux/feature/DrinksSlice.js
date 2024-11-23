import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axios";

export const fetchDrinks = createAsyncThunk(
    "drinks/fetchDrinks",
    async (_, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.get("/drinks/");
            console.log("🚀 ~ response=========>:", response);

            return response;
        } catch (e) {
            return rejectWithValue(e || "Failed to fetch drinks");
        }
    }
);

export const fetchDrinkById = createAsyncThunk(
    "drinks/fetchById",
    async (id, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.get(`/drinks/${id}`);
            return response.data;
        } catch (e) {
            return rejectWithValue(e || "Failed to fetch drink by ID");
        }
    }
);

export const createDrink = createAsyncThunk(
    "drinks/createDrink",
    async (drink, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.post(`/drinks`, drink);
            return response.data;
        } catch (e) {
            return rejectWithValue(e || "Failed to create drink");
        }
    }
);

export const updateDrink = createAsyncThunk(
    "drinks/updateDrink",
    async (drink, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.put(`/drinks`, drink);
            return response.data;
        } catch (e) {
            return rejectWithValue(e || "Failed to update drink");
        }
    }
);

export const deleteDrink = createAsyncThunk(
    "drinks/deleteDrink",
    async (id, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.delete(`/drinks/${id}`);
            console.log("success=======>");

            return response
        } catch (e) {
            console.log(e);
            return rejectWithValue(e || "Failed to delete drink");
        }
    }
);

const DrinkSlice = createSlice({
    name: "drink",
    initialState: {
        drinks: [],
        drinkById: null,
        status: null,
    },

    extraReducers: (builder) => {
        builder
            .addCase(fetchDrinks.fulfilled, (state, action) => {
                state.drinks = action.payload.data || [];
                state.paging = action.payload.paging;
                state.status = "succeeded";
            })
            .addCase(fetchDrinks.rejected, (state, action) => {
                state.status = "failed";
                state.error = action.payload;
            })
            .addCase(fetchDrinkById.fulfilled, (state, action) => {
                const packageWeddingById = action.payload.data;
                state.drinkById = packageWeddingById;
                state.status = "succeeded";
            })
            .addCase(createDrink.fulfilled, (state, action) => {
                state.drinks.push(action.payload);
                state.status = "succeeded";
            })
            .addCase(updateDrink.fulfilled, (state, action) => {
                const index = state.drinks.findIndex(
                    (pkg) => pkg.id === action.payload.id
                );
                if (index !== -1) {
                    state.drinks[index] = action.payload;
                }
                state.status = "succeeded";
            })

            .addCase(deleteDrink.fulfilled, (state, action) => {
                if (Array.isArray(state.drinks)) {
                    // Only filter if state.drinks is an array
                    state.drinks = state.drinks.filter(
                        (pkg) => pkg.id !== action.payload.id
                    );
                } else {
                    // Handle the case where state.drinks is not an array (optional)
                    console.error("state.drinks is not an array", state.drinks);
                }
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

export default DrinkSlice.reducer;
