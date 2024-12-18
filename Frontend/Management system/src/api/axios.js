import axios from "axios";

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
});

axiosInstance.interceptors.request.use(
  (config) => {
    console.log("url when request", config.baseURL + config.url);
    const token = localStorage.getItem("token");
    console.log("token", token);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    // if (error.response && error.response.status === 401) return Promise.reject(error)
    if (error.response.status === 404) {
      return error.response;
    }
    return Promise.reject(error.response.data);
  }
);
export default axiosInstance;
