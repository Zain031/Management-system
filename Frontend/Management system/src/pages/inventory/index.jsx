import { useEffect, useState } from "react";
import Container from "../../components/container";
import Header from "../../layouts/partials/header";
import { useDispatch, useSelector } from "react-redux";
import Swal from "sweetalert2";
import { Trash2 } from "lucide-react";
import { SquarePen, SquarePlus } from "lucide-react";
import {
  createInventory,
  deleteInventory,
  fetchInventories,
} from "../../redux/feature/InventorySlice";

const Inventory = () => {
  const [name, setName] = useState("");
  const [price, setPrice] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("");

  const { inventories } = useSelector((state) => state.inventories);
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchInventories());
  }, [dispatch]);

  // Filter data berdasarkan kategori yang dipilih
  const filterByCategory = (category) => {
    setSelectedCategory(category); // Set kategori yang dipilih
  };

  // Mengambil produk yang sudah difilter berdasarkan kategori
  const filteredInventories = inventories?.data?.content?.filter(
    (product) =>
      selectedCategory === "" || product.material_category === selectedCategory
  );

  // Handle delete action
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
        await dispatch(deleteInventory(id)).unwrap();
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
        await dispatch(fetchInventories());
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
      product_name: name,
      product_price: price,
      categories: "FOODS",
    };

    await dispatch(createInventory(formData));
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
    await dispatch(fetchInventories());
    setName("");
    setPrice("");
  };

  return (
    <>
      <Container>
        <Header title="Inventory" />
        <div className="flex justify-end gap-5">
          <input
            type="text"
            placeholder="Search"
            className="input input-bordered w-full max-w-xs"
          />

          <label className="form-control max-w-xs">
            <select
              className="select select-bordered"
              onChange={(e) => filterByCategory(e.target.value)}>
              <option selected value="ALL">
                All Categories
              </option>
              <option value="FOODSTUFF">Foodstuffs</option>
              <option value="TOOL">Tools</option>
              <option value="ETC">Others</option>
            </select>
          </label>

          <button
            onClick={handleAdd}
            className="tooltip"
            data-tip="Add Product">
            <SquarePlus size={50} color="#00d12a" />
          </button>
        </div>

        <dialog id="my_modal_1" className="modal">
          <div className="modal-box">
            <form onSubmit={handleSubmit}>
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
                className="input input-bordered input-ghost w-full my-2"
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
                <th>Category</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Discount (per product)</th>
                <th>Total Price</th>
                <th>User name</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {Array.isArray(filteredInventories) &&
              filteredInventories.length > 0 ? (
                filteredInventories.map((item, index) => (
                  <tr key={item.id_material}>
                    <th>{++index}</th>
                    <td>{item.material_name}</td>
                    <td>
                      <p
                        className={` ${
                          item.material_category === "FOODSTUFF"
                            ? "bg-red-600 w-14 px-2 py-1 text-white rounded-sm"
                            : item.material_category === "TOOL"
                            ? "bg-blue-600 w-14 px-2 py-1 text-white rounded-sm"
                            : item.material_category === "ETC"
                            ? "bg-green-600 w-14 px-2 py-1 text-white rounded-sm"
                            : ""
                        } font-bold`}>
                        {item.material_category === "FOODSTUFF"
                          ? "Food"
                          : item.material_category === "TOOL"
                          ? "Tool"
                          : item.material_category === "ETC"
                          ? "Other"
                          : item.material_category}
                      </p>
                    </td>

                    <td>
                      {new Intl.NumberFormat("id-ID", {
                        style: "currency",
                        currency: "IDR",
                      }).format(item.material_price_unit)}
                    </td>

                    <td>{item.material_quantity}</td>

                    <td>
                      {new Intl.NumberFormat("id-ID").format(
                        (item.material_price_discount /
                          (item.material_total_price +
                            item.material_price_discount)) *
                          100
                      )}{" "}
                      %
                    </td>

                    <td>
                      {new Intl.NumberFormat("id-ID", {
                        style: "currency",
                        currency: "IDR",
                      }).format(item.material_total_price)}
                    </td>

                    <td>{item.user.name}</td>

                    <td className="flex gap-8">
                      <button className="tooltip" data-tip="Edit">
                        <SquarePen size={28} color="#00d15b" />
                      </button>
                      <button
                        onClick={() => handleDelete(item.id_product)}
                        className=" text-white tooltip"
                        data-tip="Delete">
                        <Trash2 size={28} color="#d12a00" />
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

export default Inventory;
