class SheetMultipleTitle {
  // eslint-disable-next-line class-methods-use-this
  createHeaderTemplate(createElement, top, bottom) {
    return createElement(
      'div',
      {
        style: {
          height: '100%',
          padding: '0',
          margin: '0',
        },
      },
      createElement('div', {
        style: {
          position: 'relative',
          height: '100%',
          width: '100%',
          top: 0,
          left: 0,
        },
      }, [
        createElement(
          'span',
          {
            style: {
              position: 'absolute',
              bottom: '-10px',
              right: '0',
              fontSize: '10px',
              fontWeight: 'normal',
            },
          },
          bottom,
        ),
        createElement(
          'span',
          {
            style: {
              position: 'absolute',
              top: '-10px',
              left: '0',
              fontSize: '10px',
              fontWeight: 'normal',
            },
          },
          top,
        ),
        createElement(
          'div',
          {
            style: {
              width: '120px',
              height: '10px',
              borderBottom: '1px solid #ccc',
              transform: 'translateY(10px) translateX(-3px) rotate(155deg)',

              position: 'absolute',
              zIndex: '-1',
            },
          },
          '',
        ),
      ]),
    );
  }
}

export default new SheetMultipleTitle();
