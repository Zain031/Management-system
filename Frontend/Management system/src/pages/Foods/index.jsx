import React, { useEffect } from "react";
import Container from "../../components/container";
import Header from "../../layouts/partials/header";
import { useDispatch } from "react-redux";
import { fetchFoods } from "../../redux/feature/FoodsSlice";
import { useSelector } from "react-redux";
import { deleteFood } from "../../redux/feature/FoodsSlice";
import Swal from "sweetalert2";

const Foods = () => {
    const { foods } = useSelector((state) => state.foods);
    console.log("🚀 ~ Foods ~ foods================.:", foods);

    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(fetchFoods());
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
                await dispatch(deleteFood(id)).unwrap();

                Swal.fire({
                    position: "center",
                    icon: "success",
                    title: "Food has been deleted",
                    showConfirmButton: false,
                    timer: 1500,
                });

                dispatch(fetchFoods());
            } catch (error) {
                console.error("Deleting Failed", error);
            }
        }
    };

    return (
        <>
            <Container>
                <Header title="Foods" />
                <div className="flex justify-end">
                    <button className="btn btn-success text-white">
                        Add Foods
                    </button>
                </div>
                <div className="overflow-x-auto">
                    <table className="table">
                        <thead>
                            <tr>
                                <th></th>
                                <th>Name</th>
                                <th>Price</th>
                                <th>Total Item</th>
                                <th>Total Food Price</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {/* Check if foods is an array before mapping */}
                            {Array.isArray(foods) && foods.length > 0 ? (
                                foods.map((item, index) => (
                                    <tr key={item.id_food}>
                                        <th>{++index}</th>
                                        <td>{item.name}</td>
                                        <td>{item.food_price}</td>
                                        <td>{item.total_food}</td>
                                        <td>{item.total_food_price}</td>
                                        <td className="flex gap-2">
                                            <button className="btn btn-primary">
                                                Edit
                                            </button>
                                            <button
                                                onClick={() =>
                                                    handleDelete(item.id_food)
                                                }
                                                className="btn btn-error text-white"
                                            >
                                                Delete
                                            </button>
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="6" className="text-center">
                                        No foods available
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
