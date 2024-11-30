import { configureStore } from "@reduxjs/toolkit";
import authReducer from "../redux/feature/AuthSlice";
import foodStuffsReducer from "../redux/feature/FoodStuffsSlice";
import purchaseReducer from "../redux/feature/PurchaseSlice";

import profitsReducer from "../redux/feature/ProfitsSlice";
import salesReducer from "../redux/feature/SalesSlice";
import toolsReducer from "../redux/feature/ToolsSlice";
import foodsReducer from "../redux/feature/FoodsSlice";
import productsReducer from "../redux/feature/ProductsSlice";
import inventoryReducer from "../redux/feature/InventorySlice";

const store = configureStore({
    reducer: {
        auth: authReducer,
        foodStuffs: foodStuffsReducer,
        purchases: purchaseReducer,
        products: productsReducer,
        profits: profitsReducer,
        sales: salesReducer,
        tools: toolsReducer,
        foods: foodsReducer,
        inventories: inventoryReducer,
    },
});

export default store;
