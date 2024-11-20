import { configureStore } from "@reduxjs/toolkit";
import authSlice from "../redux/feature/AuthSlice";
import foodStuffsSlice from "../redux/feature/FoodStuffsSlice";
import purchaseSlice from "../redux/feature/PurchaseSlice";
import drinksSlice from "../redux/feature/DrinksSlice";
import profitsSlice from "../redux/feature/ProfitsSlice";
import salesSlice from "../redux/feature/SalesSlice";
import toolsSlice from "../redux/feature/ToolsSlice";
import foodSlice from "../redux/feature/FoodsSlice";

const store = configureStore({
    reducer: {
        auth: authSlice.reducer,
        foodStuffs: foodStuffsSlice.reducer,
        purchases: purchaseSlice.reducer,
        drinks: drinksSlice.reducer,
        profits: profitsSlice.reducer,
        sales: salesSlice.reducer,
        tools: toolsSlice.reducer,
        foods: foodSlice.reducer,
    },
});

export default store;
