import { createBrowserRouter } from "react-router-dom";
import Login from "../pages/Auth/Login";
import Register from "../pages/Auth/Register";
import GuestLayout from "../layouts/guest-layout";
import AppLayout from "../layouts/app-layout";
import NotFound from "../components/errors/not-found";
import Foods from "../pages/Foods";
import Drinks from "../pages/drinks";

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
                path: "foods",
                element: <Foods />,
            },
            {
                path: "drinks",
                element: <Drinks />,
            },
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
