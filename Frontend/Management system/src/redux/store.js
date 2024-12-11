import { configureStore } from "@reduxjs/toolkit";
import authReducer from "../redux/feature/AuthSlice";
import purchaseReducer from "../redux/feature/PurchaseSlice";

import profitsReducer from "../redux/feature/ProfitsSlice";
import salesReducer from "../redux/feature/SalesSlice";
import productsReducer from "../redux/feature/ProductsSlice";
import inventoryReducer from "../redux/feature/InventorySlice";
import exportReducer from "../redux/feature/exportSlice";
import dashboardReducer from "../redux/feature/dashboardSlice";
import orderReducer from "../redux/feature/orderSlice";
const store = configureStore({
  reducer: {
    auth: authReducer,
    export: exportReducer,
    purchases: purchaseReducer,
    products: productsReducer,
    profits: profitsReducer,
    sales: salesReducer,
    dashboard: dashboardReducer,
    inventories: inventoryReducer,
    order: orderReducer,
  },
});

export default store;
