import {useParams} from "react-router-dom";
import WorkOfArt from "../components/WorkOfArtComponent.tsx";

export default function WorkOfArtPage() {

    const {workOfArtId} = useParams<{ workOfArtId?: string }>();

    return (
        <div className="pb-24">
            <WorkOfArt workOfArtId={workOfArtId}/>
        </div>
    );
}
