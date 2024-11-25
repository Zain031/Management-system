import { useState } from "react";
import { useDispatch } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import { login } from "../../redux/feature/AuthSlice";
import { LockKeyhole, LockKeyholeOpen } from "lucide-react";
import logo from "../../assets/logo.jpg";

const Login = () => {
    const [key, setKey] = useState(false);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorPassword, setErrorPassword] = useState("");
    const [errorEmail, setErrorEmail] = useState("");
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const validateInputs = () => {
        let isValid = true;
        setErrorPassword("");
        setErrorEmail("");

        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const passwordPattern =
            /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;

        if (!email.trim()) {
            setErrorEmail("Email cannot be blank");
            isValid = false;
        } else if (!emailPattern.test(email)) {
            setErrorEmail("Invalid email format");
            isValid = false;
        }

        if (!password.trim()) {
            setErrorPassword("Password cannot be blank");
            isValid = false;
        } else if (password.length < 6) {
            setErrorPassword("Password must be at least 6 characters long");
            isValid = false;
        } else if (!passwordPattern.test(password)) {
            setErrorPassword(
                "Password must start with a capital letter, include a number, and a special character"
            );
            isValid = false;
        }

        return isValid;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validateInputs()) return;

        try {
            const authData = { email, password };
            await dispatch(login(authData)).unwrap();

            Swal.fire({
                icon: "success",
                title: "Signed in successfully",
                toast: true,
                position: "top-end",
                showConfirmButton: false,
                timer: 2000,
                timerProgressBar: true,
            });

            navigate("/");
        } catch (err) {
            Swal.fire({
                icon: "error",
                title: "Login failed",
                text: "Invalid email or password. Please try again.",
            });
        }
    };

    const handleKey = () => {
        setKey(!key);
    };

    return (
        <div className="flex h-screen w-full   bg-gradient-to-r from-indigo-500 via-[#acdeff] to-[#acdeff]   ">
            <div className="mb-12 mt-40 flex items-center px-8 text-center md:px-12 lg:w-1/2 lg:text-left  ">
                <div className="xs:p-0 mx-auto mt-10 rounded-lg bg-white  p-4 shadow-md outline outline-1 outline-gray-300 md:w-3/4 opacity-80">
                    <h1 className="mb-5 mt-5 text-center text-2xl font-bold">
                        Login
                    </h1>
                    <form className="px-5 py-7" onSubmit={handleSubmit}>
                        <input
                            type="text"
                            className="mb-5 w-full rounded-lg border px-3 py-3 text-sm focus:outline-none"
                            placeholder="Email"
                            value={email}
                            required
                            onChange={(e) => setEmail(e.target.value)}
                            autoComplete="off"
                        />
                        {errorEmail && (
                            <p className="mb-4 text-center text-sm text-red-600">
                                {errorEmail}
                            </p>
                        )}
                        <div className="flex gap-2">
                            <input
                                type={key ? "text" : "password"}
                                className="mb-5 w-full rounded-lg border px-3 py-3 text-sm focus:outline-none"
                                placeholder="Password"
                                value={password}
                                required
                                onChange={(e) => setPassword(e.target.value)}
                                autoComplete="off"
                            />
                            {key ? (
                                <LockKeyholeOpen
                                    onClick={handleKey}
                                    className="mt-2  cursor-pointer  "
                                    size={30}
                                    color="#000000"
                                    strokeWidth={1.25}
                                />
                            ) : (
                                <LockKeyhole
                                    onClick={handleKey}
                                    className="mt-2 cursor-pointer"
                                    size={30}
                                    color="#000000"
                                    strokeWidth={1.25}
                                />
                            )}
                        </div>

                        {errorPassword && (
                            <p className="mb-4 text-center text-sm text-red-600">
                                {errorPassword}
                            </p>
                        )}

                        <button className="w-full rounded-lg bg-blue-500 py-2.5 text-sm font-semibold text-white shadow-sm transition duration-200 hover:bg-blue-600">
                            <span className="mr-2">Login</span>
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                fill="none"
                                viewBox="0 0 24 24"
                                stroke="currentColor"
                                className="inline-block h-4 w-4"
                            >
                                <path
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    strokeWidth={2}
                                    d="M17 8l4 4m0 0l-4 4m4-4H3"
                                />
                            </svg>
                        </button>
                    </form>
                </div>
            </div>

            <div
                className="relative hidden items-center justify-center lg:flex lg:w-1/2 brightness-150"
                style={{
                    clipPath: "polygon(10% 0, 100% 0%, 100% 100%, 0 100%)",
                    backgroundImage: `url(${logo})`,
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                }}
            >
                <div className="absolute inset-0 bg-black opacity-30" />
            </div>
        </div>
    );
};

export default Login;
