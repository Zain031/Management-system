import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axios";

// Login action
export const login = createAsyncThunk(
    "auth/login",
    async (data, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.post(`/auth/login`, data);
            return response.data;
        } catch (e) {
            return rejectWithValue(e.response?.data || "Login failed");
        }
    }
);

// Register action
export const register = createAsyncThunk(
    "auth/register",
    async (data, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.post(`/auth/register`, data);
            return response.data;
        } catch (e) {
            return rejectWithValue(e.response?.data || "Registration failed");
        }
    }
);

// Fetch all users
export const fetchUsers = createAsyncThunk(
    "auth/fetchUsers",
    async (_, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.get(`/auth/users`);
            return response.data;
        } catch (e) {
            return rejectWithValue(e.response?.data || "Failed to fetch users");
        }
    }
);

// Fetch user by ID
export const fetchUsersById = createAsyncThunk(
    "auth/fetchUsersById",
    async (id, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.get(`/auth/users/${id}`);
            return response.data;
        } catch (e) {
            return rejectWithValue(e.response?.data || `Failed to fetch user with ID ${id}`);
        }
    }
);

// Update user by ID
export const updateUserById = createAsyncThunk(
    "auth/updateUserById",
    async ({ id, data }, { rejectWithValue }) => {
        try {
            const response = await axiosInstance.put(`/auth/users/${id}`, data);
            return response.data;
        } catch (e) {
            return rejectWithValue(e.response?.data || `Failed to update user with ID ${id}`);
        }
    }
);

// Auth slice
const authSlice = createSlice({
    name: "auth",
    initialState: {
        isLogin: !!localStorage.getItem("token"),
        token: localStorage.getItem("token") || null,
        role: localStorage.getItem("role") || null,
        user: JSON.parse(localStorage.getItem("user")) || null,
        users: [], // List of users
        status: {
            login: null,
            register: null,
            fetchUsers: null,
            fetchUserById: null,
            updateUserById: null,
        },
        error: null,
    },
    reducers: {
        setAuth: (state, action) => {
            const { token, role, user } = action.payload;
            state.token = token;
            state.isLogin = !!token;
            state.role = role;
            state.user = user;
            localStorage.setItem("token", token);
            localStorage.setItem("role", role);
            if (user) {
                localStorage.setItem("user", JSON.stringify(user));
            }
        },
        logout: (state) => {
            state.isLogin = false;
            state.token = null;
            state.role = null;
            state.user = null;
            localStorage.removeItem("token");
            localStorage.removeItem("role");
            localStorage.removeItem("user");
        },
    },
    extraReducers: (builder) => {
        builder
            // Login
            .addCase(login.pending, (state) => {
                state.status.login = "loading";
            })
            .addCase(login.fulfilled, (state, action) => {
                const { token, role, user } = action.payload.data;
                state.isLogin = true;
                state.token = token;
                state.role = role;
                state.user = user;
                state.status.login = "succeeded";
            })
            .addCase(login.rejected, (state, action) => {
                state.status.login = "failed";
                state.error = action.payload;
            })
            // Register
            .addCase(register.pending, (state) => {
                state.status.register = "loading";
            })
            .addCase(register.fulfilled, (state) => {
                state.status.register = "succeeded";
            })
            .addCase(register.rejected, (state, action) => {
                state.status.register = "failed";
                state.error = action.payload;
            })
            // Fetch Users
            .addCase(fetchUsers.pending, (state) => {
                state.status.fetchUsers = "loading";
            })
            .addCase(fetchUsers.fulfilled, (state, action) => {
                state.status.fetchUsers = "succeeded";
                state.users = action.payload.data;
            })
            .addCase(fetchUsers.rejected, (state, action) => {
                state.status.fetchUsers = "failed";
                state.error = action.payload;
            })
            // Fetch User By ID
            .addCase(fetchUsersById.pending, (state) => {
                state.status.fetchUserById = "loading";
            })
            .addCase(fetchUsersById.fulfilled, (state, action) => {
                state.status.fetchUserById = "succeeded";
                state.user = action.payload;
            })
            .addCase(fetchUsersById.rejected, (state, action) => {
                state.status.fetchUserById = "failed";
                state.error = action.payload;
            })
            // Update User By ID
            .addCase(updateUserById.pending, (state) => {
                state.status.updateUserById = "loading";
            })
            .addCase(updateUserById.fulfilled, (state, action) => {
                state.status.updateUserById = "succeeded";
                if (state.user?.id === action.payload.id) {
                    state.user = { ...state.user, ...action.payload };
                }
            })
            .addCase(updateUserById.rejected, (state, action) => {
                state.status.updateUserById = "failed";
                state.error = action.payload;
            });
    },
});

export const { setAuth, logout } = authSlice.actions;
export default authSlice.reducer;
