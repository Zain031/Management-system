import { NavLink } from "react-router-dom";
import {
  BaggageClaim,
  LayoutDashboard,
  PackageSearch,
  ShoppingCart,
  UserPlus,
} from "lucide-react";

function SidebarMenu() {
  return (
    <ul className="space-y-5">
      <li>
        <NavLink
          to="/"
          className="flex items-center space-x-3 text-gray-700 hover:text-white hover:bg-blue-500 dark:text-gray-300 dark:hover:text-white dark:hover:bg-blue-400 transition-all duration-200 rounded-lg p-3 text-base"
        >
          <LayoutDashboard size={30} strokeWidth={1.5} />
          <span className="font-bold">Dashboard</span>
        </NavLink>
      </li>
      <li>
        <NavLink
          to="/products"
          className="flex items-center space-x-3 text-gray-700 hover:text-white hover:bg-blue-500 dark:text-gray-300 dark:hover:text-white dark:hover:bg-blue-400 transition-all duration-200 rounded-lg p-3 text-base"
        >
          <PackageSearch size={30} strokeWidth={1.5} />
          <span className="font-bold">Products</span>
        </NavLink>
      </li>
      <li>
        <NavLink
          to="/inventory"
          className="flex items-center space-x-3 text-gray-700 hover:text-white hover:bg-blue-500 dark:text-gray-300 dark:hover:text-white dark:hover:bg-blue-400 transition-all duration-200 rounded-lg p-3 text-base"
        >
          <BaggageClaim size={30} strokeWidth={1.5} />
          <span className="font-bold">Inventory</span>
        </NavLink>
      </li>
      <li>
        <NavLink
          to="/users"
          className="flex items-center space-x-3 text-gray-700 hover:text-white hover:bg-blue-500 dark:text-gray-300 dark:hover:text-white dark:hover:bg-blue-400 transition-all duration-200 rounded-lg p-3 text-base"
        >
          <UserPlus size={30} strokeWidth={1.5} />
          <span className="font-bold">User</span>
        </NavLink>
      </li>
      <li>
        <NavLink
          to="/order"
          className="flex items-center space-x-3 text-gray-700 hover:text-white hover:bg-blue-500 dark:text-gray-300 dark:hover:text-white dark:hover:bg-blue-400 transition-all duration-200 rounded-lg p-3 text-base"
        >
          <ShoppingCart size={30} strokeWidth={1.5} />
          <span className="font-bold">Order</span>
        </NavLink>
      </li>
    </ul>
  );
}

export default SidebarMenu;
