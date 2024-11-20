import { HomeSVG } from "../../assets/svgs";
import StateSvg from "../../assets/svgs/state-svg";
import { NavLink } from "react-router-dom";
import { BellPlus } from "lucide-react";

function SidebarMenu() {
    return (
        <>
            <li>
                <NavLink to="/">
                    <HomeSVG />
                    List of Countries
                </NavLink>
            </li>

            <li>
                <NavLink to="/State-cooperation">
                    <StateSvg />
                    State Cooperation
                </NavLink>
            </li>
        </>
    );
}

export default SidebarMenu;
