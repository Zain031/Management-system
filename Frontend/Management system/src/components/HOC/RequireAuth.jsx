import { Navigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { setAuth } from "../../redux/feature/AuthSlice";
import { useEffect } from "react";
import { jwtDecode } from "jwt-decode";

const RequireAuth = ({ children }) => {
  const dispatch = useDispatch();
  const { isLogin } = useSelector((state) => state.auth);

  useEffect(() => {
    if (!isLogin) {
      const token = localStorage.getItem("token");
      const role = localStorage.getItem("role");
      const user = JSON.parse(localStorage.getItem("user"));

      const roles = ["ADMIN", "SUPER_ADMIN"];

      const getKeepLogin = async () => {
        if (!token || !role || !user) {
          return false;
        }

        const jwt = jwtDecode(token);

        if (!roles.includes(role)) {
          return false;
        }

        const isTokenExpired = jwt.exp < Date.now() / 1000;

        if (isTokenExpired) {
          localStorage.removeItem("token");
          return false;
        }
        dispatch(setAuth({ token, role, user }));
      };
      getKeepLogin();
    }
  }, [dispatch]);

  if (!isLogin) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default RequireAuth;
