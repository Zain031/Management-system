import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import { login } from "../../redux/feature/AuthSlice";
import { LockKeyhole, LockKeyholeOpen } from "lucide-react";
import logo from "../../assets/logo.jpg";
import {
  isEmailValid,
  isStrongPassword,
} from "../../../utils/validation/inputValidation";

const Login = () => {
  const [key, setKey] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorPassword, setErrorPassword] = useState("");
  const [errorEmail, setErrorEmail] = useState("");
  const { isLogin } = useSelector((state) => state.auth);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    if (isLogin) {
      navigate("/");
    }
  }, [isLogin, navigate]);

  const validateInputs = () => {
    let isValid = true;
    setErrorPassword("");
    setErrorEmail("");

    if (!email.trim()) {
      setErrorEmail("Email cannot be blank");
      isValid = false;
    } else if (!isEmailValid(email)) {
      setErrorEmail("Invalid email format");
      isValid = false;
    }

    if (!password.trim()) {
      setErrorPassword("Password cannot be blank");
      isValid = false;
    } else if (password.length < 8) {
      setErrorPassword("Password must be at least 8 characters long");
      isValid = false;
    } else if (!isStrongPassword(password)) {
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
      console.log(err);
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
    <div className="flex h-screen w-full bg-gradient-to-r from-indigo-600 via-indigo-400 to-indigo-300">
      <div className="flex items-center justify-center w-full h-full">
        <div className="mx-auto mt-10 p-8 rounded-xl bg-white shadow-lg w-full max-w-md sm:max-w-lg md:max-w-xl lg:max-w-2xl">
          <h1 className="text-center text-3xl font-semibold text-gray-800 mb-8">
            Login
          </h1>
          <form className="space-y-6" onSubmit={handleSubmit}>
            <div>
              <input
                type="text"
                className="w-full p-4 text-gray-700 bg-gray-100 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-200"
                placeholder="Email"
                value={email}
                required
                onChange={(e) => setEmail(e.target.value)}
                autoComplete="off"
              />
              {errorEmail && (
                <p className="mt-2 text-sm text-red-500">{errorEmail}</p>
              )}
            </div>

            <div className="flex items-center gap-2">
              <input
                type={key ? "text" : "password"}
                className="w-full p-4 text-gray-700 bg-gray-100 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-200"
                placeholder="Password"
                value={password}
                required
                onChange={(e) => setPassword(e.target.value)}
                autoComplete="off"
              />
              {key ? (
                <LockKeyholeOpen
                  onClick={handleKey}
                  className="text-gray-700 cursor-pointer"
                  size={30}
                  strokeWidth={1.5}
                />
              ) : (
                <LockKeyhole
                  onClick={handleKey}
                  className="text-gray-700 cursor-pointer"
                  size={30}
                  strokeWidth={1.5}
                />
              )}
            </div>

            {errorPassword && (
              <p className="mt-2 text-sm text-red-500">{errorPassword}</p>
            )}

            <button
              type="submit"
              className="w-full p-4 text-white bg-blue-500 rounded-lg hover:bg-blue-600 focus:ring-2 focus:ring-blue-500 transition duration-200"
            >
              Login
            </button>
          </form>
        </div>
      </div>

      {/* Gambar Background */}
      <div
        className="relative hidden lg:flex w-full h-full bg-cover bg-center dark:bg-gray-800"
        style={{
          backgroundImage: `url(${logo})`,
        }}
      >
        {/* Overlay Transparan untuk Memperjelas Gambar */}
        <div className="absolute inset-0 bg-black opacity-40 dark:opacity-50" />
      </div>
    </div>
  );
};

export default Login;
