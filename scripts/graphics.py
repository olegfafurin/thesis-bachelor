import plotly.graph_objects as go
import pandas as pd
import numpy as np


def draw(fig, filename, title, indices=(0, 1), **kwargs):
    data = pd.read_csv(filename).to_numpy()
    xs = data[:, indices[0]]
    ys = data[:, indices[1]]
    if len(indices) == 3:
        size = data[:, indices[2]]
        fig.add_trace(go.Scatter(x=xs, y=ys, name=title, marker_size=size, **kwargs))
    else:
        fig.add_trace(go.Scatter(x=xs, y=ys, name=title, **kwargs))


def main():
    draw_frt()

    greedy_avg_fig = go.Figure()
    greedy_avg_fig.add_trace(
        go.Scatter(x=list(range(2, 21)), y=list(range(2, 21)), name='upper_bound_distortion', mode="lines+markers"))
    draw(greedy_avg_fig, "res/greedy/avg_facebook.csv", "facebook<br>n~4K", mode="lines+markers")
    draw(greedy_avg_fig, "res/greedy/avg_erdos-renyi_5000_25.csv", "erdos-renyi<br>n=5000, p=1/200",
         mode="lines+markers")
    draw(greedy_avg_fig, "res/greedy/avg_scale-free_5000_2p0.csv", "scale-free<br>n~4K, base=2.0", mode="lines+markers")
    draw(greedy_avg_fig, "res/greedy/avg_scale-free_5000_2p5.csv", "scale-free<br>n~4K, base=2.5", mode="lines+markers")
    greedy_avg_fig.update_layout(
        width=1000,
        height=600,
        title={
            'text': f"Удаление коротких циклов: среднее искажение путей в графе",
            'y': 0.9,
            'x': 0.5,
            'xanchor': 'center',
            'yanchor': 'top'},
        xaxis_title="Длина цикла",
        yaxis_title="Искажение расстояния",
    )
    greedy_avg_fig.write_image("res/images/greedy/avg_combined.png")

    neighbours_fig = go.Figure()
    neighbours_fig.add_trace(
        go.Scatter(x=list(range(2, 21)), y=list(range(2, 21)), name='upper_bound_distortion', mode="lines"))
    draw_box(fig=neighbours_fig,
             filename="res/greedy/neighbours_greedy_scale-free_9000_2p1.csv",
             name="scale-free<br>n~5K, base=2.1",
             indices=(0, 2),
             default_x=None,
             boxpoints=False)

    draw_box(fig=neighbours_fig,
             filename="res/greedy/neighbours_greedy_scale-free_9000_2p4.csv",
             name="scale-free<br>n~5K, base=2.4",
             indices=(0, 2),
             default_x=None,
             boxpoints=False)

    draw_box(fig=neighbours_fig,
             filename="res/greedy/neighbours_greedy_scale-free_9000_2p7.csv",
             name="scale-free<br>n~5K, base=2.7",
             indices=(0, 2),
             default_x=None,
             boxpoints=False)

    draw_box(fig=neighbours_fig,
             filename="res/greedy/neighbours_facebook_4039_2p0.csv",
             name="facebook<br>n~4K",
             indices=(0, 2),
             default_x=None,
             boxpoints=False)

    neighbours_fig.update_layout(
        width=1000,
        height=600,
        title={
            'text': f"Растяжение ребра",
            'y': 0.9,
            'x': 0.5,
            'xanchor': 'center',
            'yanchor': 'top'},
        xaxis_title="Длина цикла",
        yaxis_title="Средняя длина пути",
        boxmode="group"
    )

    neighbours_fig.write_image("res/images/greedy/neighbours.png")
    #
    # fixed_edge_fig = go.Figure()
    # fixed_edge_fig.add_trace(
    #     go.Scatter(x=list(range(2, 11)), y=list(range(2, 11)), name='upper_bound_distortion', mode="lines"))
    #
    # draw(fixed_edge_fig, "res/fixed_greedy_scale-free_5000_1p5.csv", "scale-free<br>n~4K, base=1.5", mode="markers",
    #      opacity=0.7,
    #      indices=(0, 2, 1))
    # draw(fixed_edge_fig, "res/fixed_greedy_scale-free_5000_2p5.csv", "scale-free<br>n~4K, base=2.5", mode="markers",
    #      opacity=0.7,
    #      indices=(0, 2, 1))
    #
    # fixed_edge_fig.update_layout(
    #     width=1000,
    #     height=600,
    #     title={
    #         'text': f"Растяжение пути в зависимости от длины цикла",
    #         'y': 0.9,
    #         'x': 0.5,
    #         'xanchor': 'center',
    #         'yanchor': 'top'},
    #     xaxis_title="Длина цикла",
    #     yaxis_title="Среднее искажение пути",
    # )
    # fixed_edge_fig.write_image("res/images/fixed_edge.png")


def draw_frt():
    neighbours_frt_fig = go.Figure()
    neighbours_frt_fig.add_trace(
        go.Scatter(x=np.arange(1000, 10500, 1000), y=8 * np.log2(np.arange(1000, 10500, 1000)),
                   name='upper_bound_distortion', mode="lines"))

    draw_box(fig=neighbours_frt_fig,
             filename="res/frt/neighbours_scale-free_2p1.csv",
             default_x="base=2.1",
             indices=(0, 2),
             round=True,
             name="base=2.1",
             boxpoints=False)

    draw_box(fig=neighbours_frt_fig,
             filename="res/frt/neighbours_scale-free_2p4.csv",
             default_x="base=2.4",
             indices=(0, 2),
             round=True,
             name="base=2.4",
             boxpoints=False)

    draw_box(fig=neighbours_frt_fig,
             filename="res/frt/neighbours_scale-free_2p7.csv",
             default_x="base=2.7",
             indices=(0, 2),
             round=True,
             name="base=2.7",
             boxpoints=False)

    neighbours_frt_fig.update_layout(
        width=1000,
        height=600,
        title={
            'text': f"Алгоритм FRT на scale-free графах<br>Растяжение ребра",
            'y': 0.9,
            'x': 0.5,
            'xanchor': 'center',
            'yanchor': 'top'},
        xaxis_title="n",
        yaxis_title="Средняя длина пути",
        boxmode="group"
    )

    neighbours_frt_fig.write_image("res/images/frt/neighbours.png")

    fixed_edge_fig = go.Figure()
    # fixed_edge_fig.add_trace(
    #     go.Scatter(x=list(range(2, 11)), y=list(range(2, 11)), name='upper_bound_distortion', mode="lines"))

    draw_box(fixed_edge_fig, "res/frt/fixed_scale-free_8509_2p1.csv", 2.1, indices=(3,), name="base=2.1")
    draw_box(fixed_edge_fig, "res/frt/fixed_scale-free_8392_2p4.csv", 2.4, indices=(3,), name="base=2.4")
    draw_box(fixed_edge_fig, "res/frt/fixed_scale-free_8308_2p7.csv", 2.7, indices=(3,), name="base=2.7")

    fixed_edge_fig.update_layout(
        width=1000,
        height=600,
        title={
            'text': f"Алгоритм FRT, n~8K<br>Растяжение пути в зависимости от основания<br>показательного распределения",
            'y': 0.95,
            'x': 0.5,
            'xanchor': 'center',
            'yanchor': 'top'},
        xaxis_title="base",
        yaxis_title="Среднее искажение пути",
    )
    fixed_edge_fig.write_image("res/images/frt/fixed_edge.png")


def draw_box(fig, filename, default_x, indices=(0, 1), round=False, **kwargs):
    data = pd.read_csv(filename).to_numpy()
    ys = data[:, indices[1]] if len(indices) >= 2 else data[:, indices[0]]
    xs = data[:, indices[0]] if len(indices) >= 2 else [default_x for i in range(len(ys))]

    def round_floor(x, k):
        return x - (x % 10 ** k)

    fig.add_trace(go.Box(x=[round_floor(np.round(t, -2), 3) for t in xs] if round else xs, y=ys, **kwargs))


if __name__ == '__main__':
    main()
