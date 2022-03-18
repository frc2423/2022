// https://svgjs.dev/docs/3.0/getting-started/
import { SVG } from "@svgdotjs/svg.js";

function addElement(name, config, draw) {
  const {
    size: { width, height },
    image,
    translate = { x: 0, y: 0 },
    transformOrigin,
    children,
  } = config;
  const attr = { 
    'data-name': name, 
    'data-x': translate.x,
    'data-y': translate.y,
    style: `transform: translate(${translate.x}px, ${translate.y}px)`
  };
  if (transformOrigin) {
    attr['transform-origin'] = transformOrigin;
  }

  const element = draw
    .image(image)
    .size(width, height);

  if (children) {
    const group = draw
      .group()
      .attr(attr)
      .add(element);

    Object.entries(children).forEach(([name, elementConfig]) => {
      group.add(addElement(name, elementConfig, draw));
    });

    return group;
  }
  
  return element.attr(attr)
}

function createSvg(parent, config) {
  const {
    size: { width, height },
    scale = 1,
    children,
  } = config;
  const draw = SVG()
    .addTo(parent)
    // .size(width, height)
    .viewbox(0, 0, width, height)
    .scale(scale);
  Object.entries(children).forEach(([name, elementConfig]) => {
    addElement(name, elementConfig, draw);
  });
  return draw;
}

export default class RobotSvg {
  constructor(parent, config) {
    this._svg = createSvg(parent, config);
  }

  rotatePart(name, degrees) {
    const part = this._svg.findOne(`[data-name=${name}]`);
    if (!part) {
      return;
    }
    const x = part.attr('data-x');
    const y = part.attr('data-y');
    part.attr({
      style: `transform: translate(${x}px, ${y}px) rotate(${degrees}deg)`
    });
  }
}
