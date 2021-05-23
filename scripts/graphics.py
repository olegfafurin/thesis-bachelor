import plotly.graph_objects as go
import pandas as pd
import numpy as np


def draw(fig, filename, title):
    short_filename = filename[filename.find("/") + 1: filename.find(".")]
    data = pd.read_csv(filename).to_numpy()
    xs = data[:, 0]
    ys = data[:, 1]
    fig.add_trace(go.Scatter(x=xs, y=ys, name=title, mode="lines+markers"))


    # fig.write_image(f"res/images/{short_filename}.jpeg")


def main():

    fig = go.Figure()
    fig.add_trace(go.Scatter(x=list(range(2,17)), y=list(range(2,17)), name='upper_bound_distortion', mode="lines+markers"))
    draw(fig, "res/avg_greedy_facebook.csv", "facebook<br>n~4K")
    draw(fig, "res/avg_greedy_erdos-renyi_1000_100.csv", "erdos-renyi<br>n=1000, deg=100")
    draw(fig, "res/avg_greedy_erdos-renyi_1000_20.csv", "erdos-renyi<br>n=1000, deg=20")
    draw(fig, "res/avg_greedy_erdos-renyi_10000_30.csv", "erdos-renyi<br>n=10000, deg=30")
    fig.update_layout(
        title={
            'text': f"Удаление коротких циклов: искажение путей",
            'y': 0.9,
            'x': 0.5,
            'xanchor': 'center',
            'yanchor': 'top'},
        xaxis_title="Длина цикла",
        yaxis_title="Искажение расстояния",
    )
    fig.write_image("res/images/combined.png")


if __name__ == '__main__':
    main()
