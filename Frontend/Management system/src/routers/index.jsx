import { createBrowserRouter } from "react-router-dom";
import Login from "../pages/Auth/Login";
import Register from "../pages/Auth/Register";
import GuestLayout from "../layouts/guest-layout";
import AppLayout from "../layouts/app-layout";
import NotFound from "../components/errors/not-found";

import Products from "../pages/products";
import Inventory from "../pages/inventory";
import Users from "../pages/users";
import AdminDashboard from "../pages/dashboard";
const router = createBrowserRouter([
  {
    path: "/",
    element: <AppLayout />,
    errorElement: <NotFound />,
    children: [
      {
        path: "/",
        element: <AdminDashboard />,
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
