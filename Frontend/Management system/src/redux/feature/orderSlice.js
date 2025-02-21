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
  async ({ order, payment_method = "QRIS", amount }, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.post(`/orders/create`, order);
      let paymentData;
      if (payment_method != "QRIS") {
        paymentData = await axiosInstance.post(
          `/payment/process-payment?id_order=${response.data.data.id_order}`,
          {
            payment_method,
            amount,
          }
        );
      } else {
        paymentData = await axiosInstance.post(
          `/payment/process-payment?id_order=${response.data.data.id_order}`,
          {
            payment_method,
            amount: response.data.data.total_price,
          }
        );
      }
      return {
        createOrderData: response.data.data,
        paymentData: paymentData.data.data,
        payment: payment_method,
      };
    } catch (e) {
      return rejectWithValue(
        e.response?.data?.message || "Failed to create order"
      );
    }
  }
);

export const fetchOrderByMonth = createAsyncThunk(
  "orders/fetchByMonth",
  async ({ month, year, status, page = 1, size = 10 }, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.get(
        `/orders/month/${month}?year=${year}` +
          (status ? `&status=${status}` : "") +
          `&page=${page - 1}&size=${size}`
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

export const payOrder = createAsyncThunk(
  "orders/payOrder",
  async ({ orderId, payment_method, amount }, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.post(
        `/payment/process-payment?id_order=${orderId}`,
        {
          payment_method,
          amount,
        }
      );
      return response.data;
    } catch (e) {
      return rejectWithValue(e || "Failed to pay order");
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
    createdOrder: null,
    redirectURL: null,
    paging: {},
    page: 1,
    order: {},
    loading: false,
    error: null,
  },
  reducers: {
    setCustomerNameForCart: (state, action) => {
      state.cart.customerName = action.payload;
    },
    addProductToCart: (state, action) => {
      let { id } = action.payload;
      const quantity = parseInt(action.payload.quantity);
      const existingProduct = state.cart.orderDetails.find(
        (item) => item.id_product == id
      );

      if (existingProduct) {
        existingProduct.quantity += quantity;
      } else {
        const product = state.products.find((item) => {
          return item.id_product == id;
        });
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
      const { id } = action.payload;
      const index = state.cart.orderDetails.findIndex(
        (item) => item.id_product === id
      );
      if (index >= 0) {
        state.cart.orderDetails.splice(index, 1);
      }

      state.cart.totalPrice = state.cart.orderDetails.reduce(
        (total, item) => total + item.quantity * item.product_price,
        0
      );
    },
    changeQuantityInCart: (state, action) => {
      const { id, quantity } = action.payload;
      const existingProduct = state.cart.orderDetails.find(
        (item) => item.id_product === id
      );
      if (existingProduct) {
        existingProduct.quantity = parseInt(quantity, 10) || 0;
      }
      state.cart.totalPrice = state.cart.orderDetails.reduce(
        (total, item) => total + item.quantity * item.product_price,
        0
      );
    },
    clearProductCartState: (state) => {
      state.cart = {
        customerName: "",
        orderDetails: [],
        totalPrice: 0,
      };
    },
    setPage: (state, action) => {
      state.page = action.payload;
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
        state.paging = {
          totalElements: action.payload.data.totalElements,
          totalPages: action.payload.data.totalPages,
        };
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
        state.orders = action.payload.data.content;
        state.paging = {
          totalElements: action.payload.data.totalElements,
          totalPages: action.payload.data.totalPages,
        };
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
        const { createOrderData, paymentData, payment } = action.payload;
        if (payment == "QRIS") {
          const { redirect_url } = JSON.parse(paymentData.link_qris);
          state.redirectURL = redirect_url;
        }

        state.loading = false;
        state.createdOrder = createOrderData;
        state.orders.push(createOrderData);
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
  clearProductCartState,
  setPage,
  changeQuantityInCart,
} = orderSlice.actions;

export default orderSlice.reducer;
