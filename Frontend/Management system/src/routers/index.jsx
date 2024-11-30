import { createBrowserRouter } from "react-router-dom";
import Login from "../pages/Auth/Login";
import Register from "../pages/Auth/Register";
import GuestLayout from "../layouts/guest-layout";
import AppLayout from "../layouts/app-layout";
import NotFound from "../components/errors/not-found";
import Drinks from "../pages/drinks";
import Foods from "../pages/foods";

import FoodStuff from "../pages/foodstuff";
import { Users } from "lucide-react";


const router = createBrowserRouter([
    {
        path: "/",
        element: <AppLayout />,
        errorElement: <NotFound />,
        children: [
            {
                path: "/",
                element: <div>Dashboard</div>,
            },
            {
                path: "/foods",
                element: <Foods />,
            },
            {
                path: "/drinks",
                element: <Drinks />,
            },

            {
                path: "/Inventory",
                element: <FoodStuff/>,
            },
            {
                path: "/users",
                element: <Users />,
            }

        ],
    },
    {
        path: "/",
        element: <GuestLayout />,
        children: [
            {
                path: "login",
                element: <Login />,
            },
            {
                path: "register",
                element: <Register />,
            },
        ],
    },
]);

export default router;
