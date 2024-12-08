import { NavLink } from "react-router-dom";
import {
  BaggageClaim,
  LayoutDashboard,
  PackageSearch,
  UserPlus,
} from "lucide-react";

function SidebarMenu() {
  return (
    <>
      <li>
        <NavLink to="/">
          <LayoutDashboard size={30} strokeWidth={1.5} />
          Dashbord
        </NavLink>
      </li>
      <li>
        <NavLink to="/products">
          <PackageSearch size={30} strokeWidth={1.5} />
          Products
        </NavLink>
      </li>
      <li>
        <NavLink to="/Inventory">
          <BaggageClaim size={30} strokeWidth={1.5} />
          Inventory
        </NavLink>
      </li>
      <li>
        <NavLink to="/users">
          <UserPlus size={30} strokeWidth={1.5} />
          User
        </NavLink>
      </li>
    </>
  );
}

export default SidebarMenu;
