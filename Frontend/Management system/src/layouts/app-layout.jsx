import { useEffect, useState } from "react";

import { NavLink, Outlet } from "react-router-dom";
import { CloseSVG } from "../assets/svgs";
import Navbar from "./partials/navbar";
import SidebarMenu from "./partials/sidebar-menu";
import { LogOut } from "lucide-react";
import { logout } from "../redux/feature/AuthSlice";
import { useDispatch } from "react-redux";

function AppLayout() {
  const dispatch = useDispatch();
  const [isDark, setIsDark] = useState(
    JSON.parse(localStorage.getItem("isDark"))
  );
  const [isMenuOpen, setIsMenuOpen] = useState(true);

  useEffect(() => {
    localStorage.setItem("isDark", JSON.stringify(isDark));
    document.documentElement.setAttribute(
      "data-theme",
      isDark ? "dark" : "light"
    );
  }, [isDark]);

  return (
    <div className={`drawer ${isMenuOpen ? "lg:drawer-open" : ""}`}>
      <input
        id="my-drawer-3"
        type="checkbox"
        checked={isMenuOpen}
        readOnly
        className="drawer-toggle"
      />
      <div className="drawer-content mx-auto flex w-full max-w-7xl flex-col">
        <Navbar
          setIsMenuOpen={setIsMenuOpen}
          theme={isDark}
          toggleTheme={setIsDark}
        />
        <Outlet />
      </div>

      <div className="drawer-side ">
        <label htmlFor="my-drawer-3" className="drawer-overlay"></label>
        <ul className="menu menu-lg min-h-full w-80 space-y-4 bg-primary p-4 text-neutral-content">
          <div className="flex items-start justify-center rounded-lg">
            <div className="my-5 "></div>
            <button
              className="btn btn-circle btn-ghost btn-sm lg:hidden"
              onClick={() => setIsMenuOpen((open) => !open)}
            >
              <CloseSVG />
            </button>
          </div>
          <SidebarMenu />
          <li className="flex-1 justify-end">
            <NavLink
              to="/login"
              className="flex items-center space-x-3 text-gray-700 hover:text-white hover:bg-blue-500 dark:text-gray-300 dark:hover:text-white dark:hover:bg-blue-400 transition-all duration-200 rounded-lg p-3 text-base"
              onClick={() => dispatch(logout())}
            >
              <LogOut size={30} strokeWidth={1.5} />
              <span className="font-bold">Logout</span>
            </NavLink>
          </li>
        </ul>
      </div>
    </div>
  );
}

export default AppLayout;
