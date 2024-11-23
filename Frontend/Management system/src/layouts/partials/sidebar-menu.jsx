import { HomeSVG } from "../../assets/svgs";
import StateSvg from "../../assets/svgs/state-svg";
import { NavLink } from "react-router-dom";
import {
    BellPlus,
    Bolt,
    Coffee,
    HandPlatter,
    LayoutDashboard,
    UserPlus,
    Utensils,
    UtensilsCrossed,
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
                <NavLink to="/foods">
                    <Utensils size={30} strokeWidth={1.5} />
                    Foods
                </NavLink>
            </li>
            <li>
                <NavLink to="/drinks">
                    <Coffee size={30} strokeWidth={1.5} />
                    Drinks
                </NavLink>
            </li>{" "}
            <li>
                <NavLink to="/State-cooperation">
                    <Bolt size={30} strokeWidth={1.5} />
                    Tools
                </NavLink>
            </li>{" "}
            <li>
                <NavLink to="/State-cooperation">
                    <HandPlatter size={30} strokeWidth={1.5} />
                    FoodStuff
                </NavLink>
            </li>
            <li>
                <NavLink to="/State-cooperation">
                    <UserPlus  size={30} strokeWidth={1.5} />
                    Create Users
                </NavLink>
            </li>
        </>
    );
}

export default SidebarMenu;
