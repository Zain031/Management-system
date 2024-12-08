import { configureStore } from "@reduxjs/toolkit";
import authReducer from "../redux/feature/AuthSlice";
import purchaseReducer from "../redux/feature/PurchaseSlice";

import profitsReducer from "../redux/feature/ProfitsSlice";
import salesReducer from "../redux/feature/SalesSlice";
import productsReducer from "../redux/feature/ProductsSlice";
import inventoryReducer from "../redux/feature/InventorySlice";
import exportReducer from "../redux/feature/exportSlice";
const store = configureStore({
  reducer: {
    auth: authReducer,
    export: exportReducer,
    purchases: purchaseReducer,
    products: productsReducer,
    profits: profitsReducer,
    sales: salesReducer,
    inventories: inventoryReducer,
  },
});

export default store;
