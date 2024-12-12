import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axiosInstance from "../../api/axios";

export const fetchOrders = createAsyncThunk(
  "orders/fetchOrders",
  async ({ page = 1, size = 10 } = {}, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/orders?page=${page - 1}&size=${size}`
      );
      return response.data;
    } catch (e) {
      const errorMessage =
        e.response?.data?.message || e.message || "Failed to fetch orders";
      return rejectWithValue(errorMessage);
    }
  }
);

export const fetchOrderById = createAsyncThunk(
  "orders/fetchById",
  async (id, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(`/orders/${id}`);
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to fetch order by ID"
      );
    }
  }
);

export const createOrder = createAsyncThunk(
  "orders/createOrder",
  async (order, { rejectWithValue }) => {
    console.log("Order", order);
    try {
      const response = await axiosInstance.post(`/orders/create`, order);
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to create order"
      );
    }
  }
);

export const fetchOrderByMonth = createAsyncThunk(
  "orders/fetchByMonth",
  async ({ month, year }, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/orders/month/${month}?year=${year}`
      );
      return response.data;
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to fetch order by month"
      );
    }
  }
);

export const fetchAllProducts = createAsyncThunk(
  "orders/fetchAllProducts",
  async (_, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(`/products/list-products`);
      return response.data;
    } catch (e) {
      return rejectWithValue(e || "Failed to fetch products");
    }
  }
);

const orderSlice = createSlice({
  name: "orders",
  initialState: {
    orders: [],
    products: [],
    cart: {
      customerName: "",
      orderDetails: [],
      totalPrice: 0,
    },
    paging: {},
    order: {},
    loading: false,
    error: null,
  },
  reducers: {
    setCustomerNameForCart: (state, action) => {
      console.log(action.payload);
      state.cart.customerName = action.payload;
    },
    addProductToCart: (state, action) => {
      const { id, quantity } = action.payload;
      console.log(id, quantity);

      const existingProduct = state.cart.orderDetails.find(
        (item) => item.id_product == id
      );
      console.log("Existing product", existingProduct);

      if (existingProduct) {
        existingProduct.quantity += quantity;
      } else {
        const product = state.products.find((item) => {
          console.log(item);
          return item.id_product == id;
        });
        console.log("Product", product);
        state.cart.orderDetails.push({
          ...product,
          quantity,
        });
      }

      state.cart.totalPrice = state.cart.orderDetails.reduce(
        (total, item) => total + item.quantity * item.product_price,
        0
      );
    },
    removeProductFromCart: (state, action) => {
      const { id, quantityToRemove } = action.payload;

      const existingProduct = state.cart.orderDetails.find(
        (item) => item.id === id
      );

      if (existingProduct) {
        existingProduct.quantity -= quantityToRemove;

        if (existingProduct.quantity <= 0) {
          state.cart.orderDetails = state.cart.orderDetails.filter(
            (item) => item.id !== id
          );
        }
      }

      state.cart.totalPrice = state.cart.orderDetails.reduce(
        (total, item) => total + item.quantity * item.product_price,
        0
      );
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchOrders.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchOrders.fulfilled, (state, action) => {
        state.loading = false;
        state.orders = action.payload.data.content;
      })
      .addCase(fetchOrders.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(fetchOrderById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchOrderById.fulfilled, (state, action) => {
        state.loading = false;
        state.order = action.payload.data;
      })
      .addCase(fetchOrderById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(fetchOrderByMonth.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchOrderByMonth.fulfilled, (state, action) => {
        state.loading = false;
        state.orders = action.payload.data;
      })
      .addCase(fetchOrderByMonth.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(createOrder.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createOrder.fulfilled, (state, action) => {
        state.loading = false;
        state.orders.push(action.payload.data);
      })
      .addCase(createOrder.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(fetchAllProducts.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchAllProducts.fulfilled, (state, action) => {
        state.loading = false;
        state.products = action.payload.data;
      })
      .addCase(fetchAllProducts.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
  },
});

export const {
  setCustomerNameForCart,
  addProductToCart,
  removeProductFromCart,
} = orderSlice.actions;

export default orderSlice.reducer;
