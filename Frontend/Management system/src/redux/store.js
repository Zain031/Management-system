import { configureStore } from "@reduxjs/toolkit";
import authReducer from "../redux/feature/AuthSlice";
import foodStuffsReducer from "../redux/feature/FoodStuffsSlice";
import purchaseReducer from "../redux/feature/PurchaseSlice";
import drinksReducer from "../redux/feature/DrinksSlice";
import profitsReducer from "../redux/feature/ProfitsSlice";
import salesReducer from "../redux/feature/SalesSlice";
import toolsReducer from "../redux/feature/ToolsSlice";
import foodsReducer from "../redux/feature/FoodsSlice";

const store = configureStore({
    reducer: {
        auth: authReducer,
        foodStuffs: foodStuffsReducer,
        purchases: purchaseReducer,
        drinks: drinksReducer,
        profits: profitsReducer,
        sales: salesReducer,
        tools: toolsReducer,
        foods: foodsReducer,
    },
});

export default store;
