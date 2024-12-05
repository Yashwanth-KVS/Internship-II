import { Navigate } from "react-router-dom";
import { useContext } from "react";
import { UserContext } from "../utils/UserContext";

const PrivateRoute = ({ children, allowedUserType }) => {
  const { user } = useContext(UserContext);

  if (!user) {
    // User is not logged in
    return <Navigate to="/home" />;
  }

  if (user.userType !== allowedUserType) {
    // User is logged in but not the right type
    return <Navigate to="/home" />;
  }

  return children; // User is allowed to access the route
};

export default PrivateRoute;
