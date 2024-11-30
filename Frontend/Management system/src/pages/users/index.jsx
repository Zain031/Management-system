import React, { useEffect } from "react";
import Container from "../../components/container";
import Header from "../../layouts/partials/header";
import { useDispatch, useSelector } from "react-redux";
import Swal from "sweetalert2";
import { fetchUsers } from "../../redux/feature/AuthSlice";
import { Trash2 } from "lucide-react";
import { SquarePlus } from "lucide-react";
import { useState } from "react";

const Users = () => {
    const dispatch = useDispatch();
    const { users } = useSelector((state) => state.auth);

    const [searchTerm, setSearchTerm] = useState("");

    useEffect(() => {
        dispatch(fetchUsers());
    }, [dispatch]);

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
            Swal.fire("Deleted!", "The user has been deleted.", "success");
        }
    };

    const filteredUsers = users?.filter((user) =>
        user.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

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
                    <button
                        className="tooltip"
                        data-tip="Add User"
                    >
                        <SquarePlus size={50} color="#00d12a" />
                    </button>
                </div>

                <div className="overflow-x-auto shadow-lg outline outline-1 outline-slate-300 rounded-md">
                    <table className="table">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {Array.isArray(filteredUsers) && filteredUsers.length > 0 ? (
                                filteredUsers.map((user, index) => (
                                    <tr key={user.id}>
                                        <td>{index + 1}</td>
                                        <td>{user.name}</td>
                                        <td>{user.email}</td>
                                        <td>{user.phone}</td>
                                        <td>
                                            <div className="flex gap-4">
                                                <button
                                                    className="tooltip"
                                                    data-tip="Edit"
                                                >
                                                    <SquarePlus size={28} color="#00d15b" />
                                                </button>
                                                <button
                                                    onClick={() => handleDelete(user.id)}
                                                    className="tooltip"
                                                    data-tip="Delete"
                                                >
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
            </Container>
        </>
    );
};

export default Users;
