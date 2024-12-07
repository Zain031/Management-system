import { useEffect, useState } from "react";
import Container from "../../components/container";
import Header from "../../layouts/partials/header";
import { useDispatch, useSelector } from "react-redux";
import Swal from "sweetalert2";
import {
  createProduct,
  deleteProduct,
  fetchProducts,
} from "../../redux/feature/ProductsSlice";
import { Trash2 } from "lucide-react";
import { SquarePen, SquarePlus } from "lucide-react";

const Products = () => {
  const [productId, setProductId] = useState(null);
  const [name, setName] = useState("");
  const [price, setPrice] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("");
  const [categories, setCategories] = useState("");
  const tableHeaders = ["Name", "Category", "Price", "User name", "Action"];

  const { products } = useSelector((state) => state.products);

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

        Swal.fire({
          icon: "success",
          title: "Product has been deleted",
          timer: 1500,
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
      categories,
    };

    await dispatch(createProduct(formData));
    Swal.fire({
      icon: "success",
      title: "Product has been added",
      timer: 1500,
    });

    document.getElementById("my_modal_1").close();
    await dispatch(fetchProducts());
    setProductId("");
    setName("");
    setPrice("");
    setCategories("");
  };

  const filteredProducts = products?.data?.content?.filter((product) =>
    selectedCategory ? product.categories === selectedCategory : true
  );

  return (
    <>
      <Container>
        <Header title="Products" />
        <div className="flex justify-end gap-5">
          <input
            type="text"
            placeholder="Search"
            className="input input-bordered w-full max-w-xs"
          />

          <label className="form-control max-w-xs">
            <select
              className="select select-bordered"
              onChange={(e) => setSelectedCategory(e.target.value)}>
              <option value="">All Categories</option>
              <option value="Products">Products</option>
              <option value="DRINKS">Drinks</option>
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
                {tableHeaders.map((item, index) => (
                  <th key={index}>{item}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {Array.isArray(filteredProducts) &&
              filteredProducts.length > 0 ? (
                filteredProducts.map((item, index) => (
                  <tr key={item.id_product}>
                    <th>{++index}</th>
                    <td>{item.product_name}</td>

                    <td>
                      <p
                        className={` ${
                          item.categories === "FOODS"
                            ? "bg-red-600 w-14 px-2 py-1 text-white rounded-sm text-center"
                            : item.categories === "DRINKS"
                            ? "bg-blue-600 w-14 px-2 py-1 text-white rounded-sm text-center"
                            : ""
                        } font-bold`}>
                        {item.categories === "FOODS"
                          ? "Food"
                          : item.categories === "DRINKS"
                          ? "Drinks"
                          : item.categories}
                      </p>
                    </td>
                    <td>
                      {new Intl.NumberFormat("id-ID", {
                        style: "currency",
                        currency: "IDR",
                      }).format(item.product_price)}
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

export default Products;
