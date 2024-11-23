import React, { useEffect } from "react";
import Container from "../../components/container";
import Header from "../../layouts/partials/header";
import { useDispatch } from "react-redux";
import { deleteDrink, fetchDrinks } from "../../redux/feature/DrinksSlice";
import { useSelector } from "react-redux";
import Swal from "sweetalert2";


const Drinks = () => {
    const { drinks } = useSelector((state) => state.drinks);
    console.log(drinks, "=============>");
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(fetchDrinks());
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
                await dispatch(deleteDrink(id)).unwrap();

                Swal.fire({
                    position: "center",
                    icon: "success",
                    title: "Drink has been deleted",
                    showConfirmButton: false,
                    timer: 1500,
                });

                dispatch(fetchDrinks());
            } catch (error) {
                console.error("Deleting Failed", error);
            }
        }
    };


    return (
        <>
            <Container>
                <Header title="Drinks" />
                <div className="flex justify-end">
                    <button className="btn btn-success text-white">
                        Add Drink
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
                            {/* Check if drinks.data is an array before mapping */}
                            {Array.isArray(drinks.data) &&
                            drinks.data.length > 0 ? (
                                drinks.data.map((item, index) => (
                                    <tr key={item.id_drink}>
                                        <th>{++index}</th>
                                        <td>{item.drink_name}</td>
                                        <td>{item.drink_price}</td>
                                        <td>{item.total_drink}</td>
                                        <td>{item.total_drink_price}</td>
                                        <td className="flex gap-2">
                                            <button className="btn btn-primary">
                                                Edit
                                            </button>
                                            <button onClick={() => handleDelete(item.id_drink)} className="btn btn-error text-white">
                                                Delete
                                            </button>
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="6" className="text-center">
                                        No drinks available
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

export default Drinks;
