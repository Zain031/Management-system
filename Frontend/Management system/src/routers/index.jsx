import { createBrowserRouter } from "react-router-dom";
import Login from "../pages/Auth/Login";
import Register from "../pages/Auth/Register";
import GuestLayout from "../layouts/guest-layout";
import AppLayout from "../layouts/app-layout";
import NotFound from "../components/errors/not-found";

import { Users } from "lucide-react";
import Products from "../pages/products";
import Inventory from "../pages/inventory";

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
        path: "/products",
        element: <Products />,
      },

      {
        path: "/inventory",
        element: <Inventory />,
      },
      {
        path: "/users",
        element: <Users />,
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
