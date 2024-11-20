import { createBrowserRouter } from "react-router-dom";
import AppLayout from "../layouts/app-layout";
import NotFound from "../components/errors/not-found";

const router = createBrowserRouter([
    {
        path: "/",
        element: <AppLayout />,
        errorElement: <NotFound />,
    },
]);

export default router;
