package me.udnek.jeiu.visualizer.abstraction;

import org.jetbrains.annotations.NotNull;

public interface Visualizable{
    @NotNull Visualizer getVisualizer();

    class Simple implements Visualizable{

        protected Visualizer visualizer;

        public Simple(@NotNull Visualizer visualizer){
            this.visualizer = visualizer;
        }

        @Override
        public @NotNull Visualizer getVisualizer() {
            return visualizer;
        }
    }
}
