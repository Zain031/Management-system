import React from "react";
import Container from "../../components/container";
import Header from "../../layouts/partials/header";

const Tools = () => {
    const data = [
        {
            id: 1,
            name: "Drinks 1",
            price: 10000,
            total_item: 10,
            total_food_price: 100000,
        },
        {
            id: 2,
            name: "Drinks 2",
            price: 10000,
            total_item: 10,
            total_food_price: 100000,
        },
        {
            id: 3,
            name: "Drinks 3",
            price: 10000,
            total_item: 10,
            total_food_price: 100000,
        },
        {
            id: 4,
            name: "Drinks 4",
            price: 10000,
            total_item: 10,
            total_food_price: 100000,
        },
        {
            id: 5,
            name: "Drinks 5",
            price: 10000,
            total_item: 10,
            total_food_price: 100000,
        },
        {
            id: 6,
            name: "Drinks 6",
            price: 10000,
            total_item: 10,
            total_food_price: 100000,
        },
        {
            id: 7,
            name: "Drinks 7",
            price: 10000,
            total_item: 10,
            total_food_price: 100000,
        },
        {
            id: 8,
            name: "Drinks 8",
            price: 10000,
            total_item: 10,
            total_food_price: 100000,
        },
        {
            id: 9,
            name: "Drinks 9",
            price: 10000,
            total_item: 10,
            total_food_price: 100000,
        },
        {
            id: 10,
            name: "Drinks 10",
            price: 10000,
            total_item: 10,
            total_food_price: 100000,
        },
    ];

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
                            {data.map((item) => (
                                <tr key={item.id}>
                                    <th>{item.id}</th>
                                    <td>{item.name}</td>
                                    <td>{item.price}</td>
                                    <td>{item.total_item}</td>
                                    <td>{item.total_food_price}</td>
                                    <td className="flex gap-2">
                                        <button className="btn btn-primary">
                                            Edit
                                        </button>
                                        <button className="btn btn-error text-white">
                                            Delete
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </Container>
        </>
    );
};

export default Tools;

