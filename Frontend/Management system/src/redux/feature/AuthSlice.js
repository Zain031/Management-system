import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axios";

// Helper functions
const saveToLocalStorage = (key, value) => {
  if (typeof value === "object") {
    localStorage.setItem(key, JSON.stringify(value));
  } else if (typeof value === "string") localStorage.setItem(key, value);
  else {
    localStorage.removeItem(key);
  }
};

// Login action
export const login = createAsyncThunk(
  "auth/login",
  async (data, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.post(`/auth/login`, data);

      console.log("🚀 ~ response:", response);

      return response.data;
    } catch (e) {
      console.log("Response Error", e);
      return rejectWithValue(e || "Login failed");
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
      return rejectWithValue(
        e.response?.data?.message || "Registration failed"
      );
    }
  }
);

// Fetch all users
export const fetchUsers = createAsyncThunk(
  "auth/fetchUsers",
  async ({ page = 1, size = 10 }, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/auth/users?page=${page - 1}&size=${size}`
      );
      return response.data;
    } catch (e) {
      console.log(e);

      return rejectWithValue(
        e.response?.data?.message || "Failed to fetch users"
      );
    }
  }
);

export const fetchUserByName = createAsyncThunk(
  "auth/fetchUserByName",
  async ({ name, page = 1, size = 10 }, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/auth/search/${name}?page=${page - 1}&size=${size}`
      );
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to fetch users"
      );
    }
  }
);

// Fetch user by ID
export const fetchUsersById = createAsyncThunk(
  "auth/fetchUsersById",
  async (id, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(`/auth/user/${id}`);
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || `Failed to fetch user with ID ${id}`
      );
    }
  }
);

// Update user by ID
export const updateUser = createAsyncThunk(
  "auth/updateUser",
  async ({ id, data }, { rejectWithValue }) => {
    console.log(id, data);
    try {
      const response = await axiosInstance.put(`/auth/update/${id}`, data);
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || `Failed to update user with ID ${id}`
      );
    }
  }
);

export const deleteUser = createAsyncThunk(
  "auth/deleteUser",
  async (id, { rejectWithValue }) => {
    try {
      await axiosInstance.delete(`/auth/user/${id}`);
      return id;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || `Failed to delete user with ID ${id}`
      );
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
    users: [],
    userById: null,
    paging: null,
    page: 1,
    status: {
      login: null,
      register: null,
      fetchUsers: null,
      fetchUserById: null,
      updateUser: null,
    },
    error: null,
  },
  reducers: {
    setPage: (state, action) => {
      state.page = action.payload;
    },
    setAuth: (state, action) => {
      const { token, role, user } = action.payload;
      state.token = token;
      state.isLogin = !!token;
      state.role = role;
      state.user = user;
      saveToLocalStorage("token", token);
      saveToLocalStorage("role", role);
      saveToLocalStorage("user", user);
    },
    logout: (state) => {
      state.isLogin = false;
      state.token = null;
      state.role = null;
      state.user = null;
      saveToLocalStorage("token");
      saveToLocalStorage("role");
      saveToLocalStorage("user");
    },
  },
  extraReducers: (builder) => {
    builder
      // Login
      .addCase(login.pending, (state) => {
        state.status.login = "loading";
      })
      .addCase(login.fulfilled, (state, action) => {
        const { token, user } = action.payload.data;
        const { role } = user;
        saveToLocalStorage("token", token);
        saveToLocalStorage("role", role);
        saveToLocalStorage("user", user);
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
        state.users = action.payload.data.content;
        state.paging = {
          totalElements: action.payload.data.totalElements,
          totalPages: action.payload.data.totalPages,
        };
      })
      .addCase(fetchUsers.rejected, (state, action) => {
        state.status.fetchUsers = "failed";
        state.error = action.payload;
      })
      // Fetch User By Name
      .addCase(fetchUserByName.pending, (state) => {
        state.status.fetchUsers = "loading";
      })
      .addCase(fetchUserByName.fulfilled, (state, action) => {
        state.status.fetchUsers = "succeeded";
        state.users = action.payload.data.content;
        state.paging = {
          totalElements: action.payload.data.totalElements,
          totalPages: action.payload.data.totalPages,
        };
      })
      .addCase(fetchUserByName.rejected, (state, action) => {
        state.status.fetchUsers = "failed";
        state.error = action.payload;
      })
      // Fetch User By ID
      .addCase(fetchUsersById.pending, (state) => {
        state.status.fetchUserById = "loading";
      })
      .addCase(fetchUsersById.fulfilled, (state, action) => {
        state.status.fetchUserById = "succeeded";
        state.userById = action.payload.data;
      })
      .addCase(fetchUsersById.rejected, (state, action) => {
        state.status.fetchUserById = "failed";
        state.error = action.payload;
      })
      // Update User By ID
      .addCase(updateUser.pending, (state) => {
        state.status.updateUser = "loading";
      })
      .addCase(updateUser.fulfilled, (state, action) => {
        state.status.updateUser = "succeeded";
        if (state.user?.id === action.payload.id) {
          state.user = { ...state.user, ...action.payload };
        }
      })
      .addCase(updateUser.rejected, (state, action) => {
        state.status.updateUser = "failed";
        state.error = action.payload;
      })
      .addCase(deleteUser.pending, (state) => {
        state.status.updateUser = "loading";
      })
      .addCase(deleteUser.fulfilled, (state, action) => {
        state.status.updateUser = "succeeded";
        state.users = state.users.filter(
          (user) => action.payload !== user.id_user
        );
      })
      .addCase(deleteUser.rejected, (state, action) => {
        state.status.updateUser = "failed";
        state.error = action.payload;
      });
  },
});

export const { setAuth, logout, setPage } = authSlice.actions;
export default authSlice.reducer;
