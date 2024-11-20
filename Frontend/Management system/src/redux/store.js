import { configureStore } from "@reduxjs/toolkit";

import authSlice from "./AuthSlice";
import foodStuffsSlice from "./FoodStuffsSlice";
import purchaseSlice from "./PurchasesSlice";
import drinksSlice from "./DrinksSlice";
import profitsSlice from "./ProfitsSlice";
import salesSlice from "./SalesSlice";
import toolsSlice from "./ToolsSlice";
import foodSlice from "./FoodsSlice";

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
