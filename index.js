import React from 'react';
import { requireNativeComponent, UIManager, findNodeHandle, Platform } from 'react-native';

const ImageCropperWrapper = requireNativeComponent('ImageCropper');

class ImageCropper extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  // blur = () => {
  //   UIManager.dispatchViewManagerCommand(
  //     findNodeHandle(this),
  //     UIManager.getViewManagerConfig('RichText').Commands.blur,
  //     [],
  //   );
  // }

  render() {
    return (
      <ImageCropperWrapper
        {...this.props}
      />
    );
  }
}

export default ImageCropper;