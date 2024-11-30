import React, { useEffect } from "react";
import Container from "../../components/container";
import Header from "../../layouts/partials/header";
import { useDispatch } from "react-redux";
import { useSelector } from "react-redux";
import Swal from "sweetalert2";
import {
    createProduct,
    deleteProduct,
    fetchProducts,
} from "../../redux/feature/ProductsSlice";
import { Trash2 } from "lucide-react";
import { SquarePen, SquarePlus } from "lucide-react";
import { useState } from "react";

const Foods = () => {
    const [productId, setProductId] = useState(null);
    const [name, setName] = useState("");
    const [price, setPrice] = useState("");
    const [categories, setCategories] = useState("");

    const { products } = useSelector((state) => state.products);
    console.log(products?.data?.content, "==========ggg===>");

    const foods = products?.data?.content?.filter((product) => {
        return product.categories === "FOODS";
    });
    console.log(foods);
    const dispatch = useDispatch();
    useEffect(() => {
        dispatch(fetchProducts());
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
            try {
                await dispatch(deleteProduct(id)).unwrap();

                const Toast = Swal.mixin({
                    toast: true,
                    position: "top-end",
                    showConfirmButton: false,
                    timer: 1500,
                    timerProgressBar: true,
                    didOpen: (toast) => {
                        toast.onmouseenter = Swal.stopTimer;
                        toast.onmouseleave = Swal.resumeTimer;
                    },
                });
                Toast.fire({
                    icon: "success",
                    title: "Product has been deleted",
                });

                await dispatch(fetchProducts());
            } catch (error) {
                console.log(error);

            }
        }
    };

    const handleAdd = () => {
        document.getElementById("my_modal_1").showModal();
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = {
            id_user: productId,
            product_name: name,
            product_price: price,
            categories: "FOODS",
        };

       await dispatch(createProduct(formData));
        const Toast = Swal.mixin({
            toast: true,
            position: "top-end",
            showConfirmButton: false,
            timer: 1500,
            timerProgressBar: true,
            didOpen: (toast) => {
                toast.onmouseenter = Swal.stopTimer;
                toast.onmouseleave = Swal.resumeTimer;
            },
        });
        Toast.fire({
            icon: "success",
            title: "Product has been added",
        });

        document.getElementById("my_modal_1").close();
        await dispatch(fetchProducts());
        setProductId("");
        setName("");
        setPrice("");
        setCategories("");
    };

    return (
        <>
            <Container>
                <Header title="Foods" />
                <div className="flex justify-end gap-5">
                <input type="text" placeholder="Search" className="input input-bordered w-full max-w-xs" />
                    <button
                        onClick={handleAdd}
                        className="tooltip"
                        data-tip="Add Product"
                    >
                        <SquarePlus size={50} color="#00d12a" />
                    </button>
                </div>

                <dialog id="my_modal_1" className="modal">
                    <div className="modal-box">
                        <form onSubmit={handleSubmit}>
                            <input
                                type="text"
                                value={productId}
                                placeholder="id_product"
                                className="input input-bordered input-ghost w-full my-2"
                                onChange={(e) => setProductId(e.target.value)}
                            />

                            <input
                                type="text"
                                placeholder="Product Name"
                                className="input input-bordered input-ghost w-full my-2"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                            />

                            <input
                                type="text"
                                placeholder="Product Price"
                                className="input input-bordered input-ghost w-full my-2 "
                                value={price}
                                onChange={(e) => setPrice(e.target.value)}
                            />

                            <button className="btn btn-outline btn-primary w-full">
                                Submit
                            </button>
                        </form>
                        <div className="modal-action">
                            <form method="dialog">
                                <button className="btn">Close</button>
                            </form>
                        </div>
                    </div>
                </dialog>

                <div className="overflow-x-auto shadow-lg outline outline-1 outline-slate-300 rounded-md mt-2">
                    <table className="table">
                        <thead>
                            <tr>
                                <th></th>
                                <th>Name</th>
                                <th>Price</th>
                                <th>User Name</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {Array.isArray(foods) && foods.length > 0 ? (
                                foods.map((item, index) => (
                                    <tr key={item.id_product}>
                                        <th>{++index}</th>
                                        <td>{item.product_name}</td>
                                        <td>{item.product_price}</td>
                                        <td>{item.user.name}</td>
                                        <td className="flex gap-8">
                                            <button
                                                className="tooltip"
                                                data-tip="Edit"
                                            >
                                                <SquarePen
                                                    size={28}
                                                    color="#00d15b"
                                                />
                                            </button>
                                            <button
                                                onClick={() =>
                                                    handleDelete(
                                                        item.id_product
                                                    )
                                                }
                                                className=" text-white tooltip"
                                                data-tip="Delete"
                                            >
                                                <Trash2
                                                    size={28}
                                                    color="#d12a00"
                                                />
                                            </button>
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="6" className="text-center">
                                        No products available
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

export default Foods;
