import { Navigate } from "react-router-dom";

const isAuthenticated = () => {
  return localStorage.getItem("token") !== null;
};

const RequireAuth = ({ children }) => {
  if (!isAuthenticated()) {
    return <Navigate to="/login" replace />;
  }
  return children;
};

export default RequireAuth;
