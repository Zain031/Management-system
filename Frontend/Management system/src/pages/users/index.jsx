import { useEffect } from "react";
import Container from "../../components/container";
import Header from "../../layouts/partials/header";
import { useDispatch, useSelector } from "react-redux";
import Swal from "sweetalert2";
import { Eye, EyeOff, SquarePen, Trash2 } from "lucide-react";
import { SquarePlus } from "lucide-react";
import { useState } from "react";
import {
  deleteUser,
  fetchUserByName,
  fetchUsers,
  fetchUsersById,
  register,
  updateUser,
  setPage,
} from "../../redux/feature/AuthSlice";
import { Pagination } from "@nextui-org/pagination";
import ButtonExport from "../../components/ButtonExport";
import { exportUsers } from "../../redux/feature/exportSlice";
import {
  dynamicValidation,
  isEmailValid,
  isNotEmpty,
  isStrongPassword,
  validateName,
} from "../../../utils/validation/inputValidation";

const Users = () => {
  const dispatch = useDispatch();
  const { users, userById, page, paging } = useSelector((state) => state.auth);
  const [email, setEmail] = useState("");
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("");
  const [userId, setUserId] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [errorPassword, setErrorPassword] = useState("");
  const [errorEmail, setErrorEmail] = useState("");
  const [errorName, setErrorName] = useState("");
  const [errorRole, setErrorRole] = useState("");
  const [key, setKey] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
    dispatch(fetchUsers({ page: 1 }));
  }, []);

  useEffect(() => {
    dispatch(setPage(1));
    if (searchTerm.trim().length === 0) {
      dispatch(fetchUsers({ page: 1 })).unwrap();
    } else {
      dispatch(fetchUserByName({ name: searchTerm, page: 1 }));
    }
  }, [searchTerm, dispatch]);

  useEffect(() => {
    dispatch(fetchUsers({ page }));
  }, [page, dispatch]);

  const handlePageChange = (newPage) => {
    dispatch(setPage(newPage));
  };
  useEffect(() => {
    console.log("userById", userById);
    if (isEditing) {
      setName(userById?.name || "");
      setEmail(userById?.email || "");
      setRole(userById?.role);
    }
  }, [userById, dispatch]);

  useEffect(() => {
    dispatch(fetchUsers({ page: page }));
  }, [page, dispatch]);

  const handleKey = () => {
    setKey(!key);
  };
  const handleDelete = async (id) => {
    const result = await Swal.fire({
      title: "Are you sure?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, delete it!",
    });

    if (result.isConfirmed) {
      // Handle user deletion here if needed.
      await dispatch(deleteUser(id));
      Swal.fire("Deleted!", "The user has been deleted.", "success");
    }
  };

  const resetForm = () => {
    setEmail("");
    setName("");
    setPassword("");
    setRole("");
  };

  const onButtonEditClick = (id) => {
    setIsEditing(true);
    document.getElementById("modal_form_User").showModal();
    setUserId(id);
    dispatch(fetchUsersById(id));
  };
  const handleEdit = async ({ id, data }) => {
    const result = await Swal.fire({
      title: "Are you sure?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, update it!",
    });
    if (result.isConfirmed) {
      try {
        await dispatch(updateUser({ id, data })).unwrap();
        setIsEditing(false);

        Swal.fire({
          icon: "success",
          title: "User has been updated",
          timer: 1500,
        });
        document.getElementById("modal_form_User").close();
        await dispatch(fetchUsers());
      } catch (error) {
        console.log(error);
      }
    }
    dispatch(setPage(1));
    dispatch(fetchUsers({ page: 1 }));
  };

  const onButtonAddClick = () => {
    document.getElementById("modal_form_User").showModal();
  };
  const handleAdd = async (data) => {
    await dispatch(register(data));
    Swal.fire({
      icon: "success",
      title: "User has been added",
      timer: 1500,
    });

    document.getElementById("modal_form_User").close();
    await dispatch(fetchUsers({ page })).unwrap();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const data = {
      email,
      name,
      password,
      role,
    };
    const validation = [
      {
        name: "name",
        validator: (name) => {
          return isNotEmpty(name) && validateName(name);
        },
        negativeImpact: () => setErrorName("Name cannot be blank"),
        positiveImpact: () => setErrorName(""),
      },
      {
        name: "password",
        validator: (password) => {
          return isStrongPassword(password);
        },
        negativeImpact: () =>
          setErrorPassword(
            "please enter strong password, include a number, and a special character"
          ),
        positiveImpact: () => setErrorPassword(""),
      },
      {
        name: "email",
        validator: (email) => {
          return isNotEmpty(email) && isEmailValid(email);
        },
        negativeImpact: () =>
          setErrorEmail("Email cannot be blank and must be valid"),
        positiveImpact: () => setErrorEmail(""),
      },
      {
        name: "role",
        validator: (role) => {
          return role.length > 0;
        },
        negativeImpact: () => setErrorRole("Role cannot be blank"),
        positiveImpact: () => setErrorRole(""),
      },
    ];
    let allValid = dynamicValidation(validation, data);
    if (!allValid) {
      return;
    }
    document.getElementById("modal_form_User").close();

    if (isEditing) {
      handleEdit({ id: userId, data });
    } else {
      handleAdd(data);
    }
    resetForm();
  };

  return (
    <>
      <Container>
        <Header title="Users" />
        <div className="flex justify-end gap-5 mb-4">
          <input
            type="text"
            placeholder="Search by name"
            className="input input-bordered w-full max-w-xs"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <ButtonExport onPress={() => dispatch(exportUsers())}>
            Export Users
          </ButtonExport>
          <button
            className="tooltip"
            data-tip="Add User"
            onClick={onButtonAddClick}>
            <SquarePlus size={50} color="#00d12a" />
          </button>
        </div>

        <dialog id="modal_form_User" className="modal">
          <div className="modal-box">
            <form onSubmit={handleSubmit}>
              <input
                type="text"
                placeholder="Name"
                className="input input-bordered input-ghost w-full my-2"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
              {errorName && (
                <span className="text-red-500 text-sm">{errorName}</span>
              )}
              <input
                type="email"
                placeholder="Email"
                className="input input-bordered input-ghost w-full my-2"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
              {errorEmail && (
                <span className="text-red-500 text-sm">{errorEmail}</span>
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
                  <EyeOff
                    onClick={handleKey}
                    className="mt-2 cursor-pointer"
                    size={30}
                    color="#000000"
                    strokeWidth={1.25}
                  />
                ) : (
                  <Eye
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

              <select
                className="select select-bordered w-full my-2"
                value={role}
                onChange={(e) => setRole(e.target.value)}>
                <option value="">Select Role</option>
                <option value="SUPER_ADMIN">Super Admin</option>
                <option value="ADMIN">Admin</option>
              </select>
              {errorRole && (
                <span className="text-red-500 text-sm">{errorRole}</span>
              )}

              <button className="btn btn-outline btn-primary w-full">
                Submit
              </button>
            </form>
            <div className="modal-action">
              <form method="dialog">
                <button
                  className="btn"
                  onClick={() => {
                    setIsEditing(false);
                    resetForm();
                  }}>
                  Close
                </button>
              </form>
            </div>
          </div>
        </dialog>

        <div className="overflow-x-auto shadow-lg outline outline-1 outline-slate-300 rounded-md">
          <table className="table">
            <thead>
              <tr>
                <th></th>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Created At</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {Array.isArray(users) && users.length > 0 ? (
                users.map((user, index) => (
                  <tr key={user.id_user}>
                    <td>{index + 1}</td>
                    <td>{user.name}</td>
                    <td>{user.email}</td>
                    <td>
                      <p
                        className={`${
                          user.role === "SUPER_ADMIN"
                            ? "bg-blue-600 w-24 px-2 py-1 text-white rounded-md text-center"
                            : user.role === "ADMIN"
                            ? "bg-red-600 w-24 px-2 py-1 text-white rounded-md text-center"
                            : user.role === "CUSTOMER"
                        } font-bold text-center`}>
                        {user.role === "SUPER_ADMIN"
                          ? "Super Admin"
                          : user.role === "ADMIN"
                          ? "Admin"
                          : "Customer"}
                      </p>
                    </td>
                    <td>{user.created_at.split("T")[0]}</td>
                    <td>
                      <div className="flex gap-4">
                        <button
                          className="tooltip"
                          data-tip="Edit"
                          onClick={() => onButtonEditClick(user.id_user)}>
                          <SquarePen size={28} color="#00d15b" />
                        </button>
                        <button
                          onClick={() => handleDelete(user.id_user)}
                          className="tooltip"
                          data-tip="Delete">
                          <Trash2 size={28} color="#d12a00" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5" className="text-center">
                    No users found
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
        {users && users.length > 0 && paging && paging.totalPages > 1 && (
          <div className="my-6">
            <Pagination
              initialPage={page}
              total={paging?.totalPages}
              onChange={handlePageChange}
            />
          </div>
        )}
      </Container>
    </>
  );
};

export default Users;
